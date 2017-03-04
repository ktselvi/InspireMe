package com.ktselvi.inspireme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ktselvi.inspireme.network.NetworkAccess;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCategoriesReference;
    private ValueEventListener categoriesListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<String> mCategoriesList;
    private String ITEM_CATEGORIES = "Categories";
    private String ITEM_AUTHORS = "Authors";
    private String ITEM_FAVOURITES = "Favourites";
    private String ITEM_ID = "Navigation drawer item";
    private SharedPreferences mPreferences;
    private String FIRST_USAGE_TAG = "FirstUsage";

    private BroadcastReceiver mConnectionReceiver;

    @BindView(R.id.no_network_error)
    TextView noNetworkErrorView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategoriesList = new ArrayList<>();
        //Initializing UI elements
        setUpUi();

        //Checking if this is the first time that the user is opening this app after installation
        //If this is the first usage, then we need to check for internet connectivity so as to get the data from Firebase
        //If not the first usage, data can still be fetched without internet connectivity due to offline storage of firebase data
        mPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        if(mPreferences.getBoolean(FIRST_USAGE_TAG, true)){
            if(NetworkAccess.isConnectedToNetwork(this)){
                progressBarView.setVisibility(View.VISIBLE);
                //Setting up Firebase
                initializeFirebase();
                //Starting the Async Task to fetch data
                new CategoriesAsyncTask().execute();
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
            //Add listeners for firebase references
            getCategoriesData();
        }
    }

    //This method creates and registers a BroadcastReceiver for listening to network changes
    private void setConnectivityReceiver() {
        mConnectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(NetworkAccess.isConnectedToNetwork(context)){
                    noNetworkErrorView.setVisibility(View.INVISIBLE);
                    progressBarView.setVisibility(View.VISIBLE);
                    //Setting up Firebase
                    initializeFirebase();
                    //Starting the Async Task to fetch data
                    new CategoriesAsyncTask().execute();
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * This method initializes the firebase database and analytics references and sets the correct properties
     */
    private void initializeFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
        mCategoriesReference = mFirebaseDatabase.getReference("categories");
        mCategoriesReference.keepSynced(true);

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
        } else if (id == R.id.nav_authors) {
            // Log the select event using firebase analytics
            logAnalyticEvent(ITEM_AUTHORS);
        } else if (id == R.id.nav_fav) {
            // Log the select event using firebase analytics
            logAnalyticEvent(ITEM_FAVOURITES);
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

    private class CategoriesAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getCategoriesData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBarView.setVisibility(View.INVISIBLE);
            mPreferences.edit().putBoolean(FIRST_USAGE_TAG, false).apply();
        }
    }

    /**
     * Getting the categories data from Firebase database
     */
    private void getCategoriesData(){
        categoriesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCategoriesList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    mCategoriesList.add((String) child.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseCrash.log("getCategoriesData - Could not fetch data : "+databaseError.toString());
            }
        };
        mCategoriesReference.addValueEventListener(categoriesListener);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mCategoriesReference != null && categoriesListener!= null){
            mCategoriesReference.removeEventListener(categoriesListener);
        }

        if(mConnectionReceiver != null){
            unregisterReceiver(mConnectionReceiver);
            mConnectionReceiver = null;
        }
    }
}
