package com.ktselvi.inspireme;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ktselvi.inspireme.fragments.AuthorsFragment;
import com.ktselvi.inspireme.fragments.CategoriesListFragment;
import com.ktselvi.inspireme.handlers.AuthorClickListener;
import com.ktselvi.inspireme.handlers.CategoryClickListener;
import com.ktselvi.inspireme.model.Author;
import com.ktselvi.inspireme.model.Quote;
import com.ktselvi.inspireme.network.NetworkAccess;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,CategoryClickListener,AuthorClickListener {

    //This is to avoid the "Calls to setPersistenceEnabled() must be made before any other usage of FirebaseDatabase instance" exception
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCategoriesReference;
    private ValueEventListener categoriesListener;
    private DatabaseReference mAuthorsReference;
    private ValueEventListener authorsListener;
    private DatabaseReference mQuotesReference;
    private ValueEventListener quotesListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<String> mCategoriesList;
    private ArrayList<Author> mAuthorsList;
    private ArrayList<Quote> mQuotesList;

    private String ITEM_CATEGORIES = "Categories";
    private String ITEM_AUTHORS = "Authors";
    private String ITEM_FAVOURITES = "Favourites";
    private String ITEM_ID = "Navigation drawer item";
    private SharedPreferences mPreferences;
    private String FIRST_USAGE_TAG = "FirstUsage";
    private String FRAGMENT_TAG = "ListFragment";
    private String KEY_VIEW_TYPE = "ViewType";
    private String KEY_SELECTED_VALUE = "Value";

    private BroadcastReceiver mConnectionReceiver;
    private CategoriesListFragment categoriesListFragment;
    private AuthorsFragment authorsListFragment;
    private ProgressDialog mLoadingDialog;

    @BindView(R.id.no_network_error)
    TextView noNetworkErrorView;

    @BindView((R.id.nav_view))
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategoriesList = new ArrayList<>();
        mAuthorsList = new ArrayList<>();
        mQuotesList = new ArrayList<>();

        //Loading indicator using Progress dialog till the data is fetched
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setMessage(getString(R.string.loading_message));
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //Initializing UI elements
        setUpUi();

        //Checking if this is the first time that the user is opening this app after installation
        //If this is the first usage, then we need to check for internet connectivity so as to get the data from Firebase
        //If not the first usage, data can still be fetched without internet connectivity due to offline storage of firebase data
        mPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        if(mPreferences.getBoolean(FIRST_USAGE_TAG, true)){
            if(NetworkAccess.isConnectedToNetwork(this)){
                mLoadingDialog.show();
                //Setting up Firebase
                initializeFirebase();
                //Starting the Async Task to fetch data
                new DataAsyncTask().execute();
            }
            else{
                noNetworkErrorView.setVisibility(View.VISIBLE);
                //Listening to network changes
                setConnectivityReceiver();
            }
        }
        else {
            //Setting up Firebase
            initializeFirebase();
            //Add listeners for firebase references and draw the UI as the categories view is the default view when the activity is created
            getCategoriesData(true);
        }
    }

    //This method creates and registers a BroadcastReceiver for listening to network changes
    private void setConnectivityReceiver() {
        mConnectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(NetworkAccess.isConnectedToNetwork(context)){
                    noNetworkErrorView.setVisibility(View.INVISIBLE);
                    mLoadingDialog.show();
                    //Setting up Firebase
                    initializeFirebase();
                    //Starting the Async Task to fetch data
                    new DataAsyncTask().execute();
                    unregisterReceiver(mConnectionReceiver);
                    mConnectionReceiver = null;
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(mConnectionReceiver, filter);
    }

    private void setUpUi() {
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_category);
    }

    /**
     * This method initializes the firebase database and analytics references and sets the correct properties
     */
    private void initializeFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCategoriesReference = mFirebaseDatabase.getReference("categories");
        mCategoriesReference.keepSynced(true);
        mAuthorsReference = mFirebaseDatabase.getReference("authors");
        mAuthorsReference.keepSynced(true);
        mQuotesReference = mFirebaseDatabase.getReference("quotes");
        mQuotesReference.keepSynced(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_category) {
            // Log the select event using firebase analytics
            logAnalyticEvent(ITEM_CATEGORIES);
            //If there is no data, then fetch the data and after receiving the data, draw the UI
            if(mCategoriesList.size() == 0){
                getCategoriesData(true);
            }
            else {
                //There is data, so need to fetch again, just draw the UI
                addCategoriesListView();
            }
        } else if (id == R.id.nav_authors) {
            // Log the select event using firebase analytics
            logAnalyticEvent(ITEM_AUTHORS);
            //If there is no data, then fetch the data and after receiving the data, draw the UI
            if(mAuthorsList.size() == 0){
                getAuthorsData(true);
            }
            else{
                //There is data, so need to fetch again, just draw the UI
                addAuthorsListView();
            }
        } else if (id == R.id.nav_fav) {
            // Log the select event using firebase analytics
            logAnalyticEvent(ITEM_FAVOURITES);
            navigateToFav();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logAnalyticEvent(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ITEM_ID);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * Click listener for authors list
     * It is called when a click action is performed on the profile picture of the author
     * It initiates the QuotesListActivity with the author name
     * @param authorName
     */
    @Override
    public void handleAuthorClicked(String authorName) {
        Intent intent = new Intent(this, QuotesListActivity.class);
        intent.putExtra(KEY_VIEW_TYPE, "authors");
        intent.putExtra(KEY_SELECTED_VALUE, authorName);
        startActivity(intent);
    }

    /**
     * Click listener for categories list
     * It is called when a click action is performed on category name
     * It initiates the QuotesListActivity with the category name
     * @param categoryName
     */
    @Override
    public void handleCategoryClicked(String categoryName) {
        Intent intent = new Intent(this, QuotesListActivity.class);
        intent.putExtra(KEY_VIEW_TYPE, "categories");
        intent.putExtra(KEY_SELECTED_VALUE, categoryName);
        startActivity(intent);
    }

    private class DataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //As the Async task is called only if it is first run, we pass drawUI as true to getCategoriesData
            //This is because categories view is the default view when the activity is launched. Hence we want to fetch the data and draw the UI with the results
            getCategoriesData(true);
            getAuthorsData(false);
            getQuotesData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //mLoadingDialog.dismiss();
            mPreferences.edit().putBoolean(FIRST_USAGE_TAG, false).apply();
        }
    }

    /**
     * Getting the categories data from Firebase database
     */
    private void getCategoriesData(final boolean drawUI){
        if(categoriesListener == null){
            categoriesListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mCategoriesList.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        mCategoriesList.add((String) child.getValue());
                    }
                    if(drawUI){
                        addCategoriesListView();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    FirebaseCrash.log("getCategoriesData - Could not fetch data : "+databaseError.toString());
                }
            };
            mCategoriesReference.addValueEventListener(categoriesListener);
        }
    }

    /**
     * Getting the quotes data from Firebase database
     */
    private void getQuotesData(){
        if(quotesListener == null){
            quotesListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mQuotesList.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Quote quote = child.getValue(Quote.class);
                        mQuotesList.add(quote);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    FirebaseCrash.log("getQuotesData - Could not fetch data : "+databaseError.toString());
                }
            };
            mQuotesReference.addValueEventListener(quotesListener);
        }
    }

    /**
     * Adds the fragment to display categories list view
     */
    private void addCategoriesListView() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        categoriesListFragment = new CategoriesListFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("CAT_LIST", mCategoriesList);
        categoriesListFragment.setArguments(bundle);

        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }

        ft.replace(R.id.fragment_holder, categoriesListFragment, FRAGMENT_TAG).commit();
    }

    /**
     * Getting the authors data from Firebase database
     */
    private void getAuthorsData(final boolean drawUI){
        if(authorsListener == null){
            authorsListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mAuthorsList.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Author a = child.getValue(Author.class);
                        mAuthorsList.add(a);
                    }
                    if(drawUI){
                        addAuthorsListView();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    FirebaseCrash.log("getAuthorsData - Could not fetch data : "+databaseError.toString());
                }
            };
            mAuthorsReference.addValueEventListener(authorsListener);
        }
    }

    /**
     * Adds the fragment to display authors list view
     */
    private void addAuthorsListView() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        authorsListFragment = new AuthorsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("AUTHORS_LIST", mAuthorsList);
        authorsListFragment.setArguments(bundle);

        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }

        ft.replace(R.id.fragment_holder, authorsListFragment, FRAGMENT_TAG).commit();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mCategoriesReference != null && categoriesListener!= null){
            mCategoriesReference.removeEventListener(categoriesListener);
        }
        if(mAuthorsReference != null && authorsListener!= null){
            mAuthorsReference.removeEventListener(authorsListener);
        }
        if(mQuotesReference != null && quotesListener!= null){
            mQuotesReference.removeEventListener(quotesListener);
        }

        if(mConnectionReceiver != null){
            unregisterReceiver(mConnectionReceiver);
            mConnectionReceiver = null;
        }
    }

    /**
     * Navigate to Favourite quotes activity
     */
    private void navigateToFav(){
        Intent intent = new Intent(this, FavQuotesActivity.class);
        startActivity(intent);
    }

    /**
     * This method is exposed so that the fragments can set the correct selection in the navigation drawer.
     * This is done so as to handle the cases of back button/up button pressed.
     * @param id
     */
    public void setNavDrawerCheckedItem(int id){
        navigationView.setCheckedItem(id);
    }
}
