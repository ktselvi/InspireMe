package com.ktselvi.inspireme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ktselvi.inspireme.fragments.QuotesListFragment;
import com.ktselvi.inspireme.model.Quote;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class QuotesListActivity extends AppCompatActivity {

    private String KEY_VIEW_TYPE = "ViewType";
    private String KEY_SELECTED_VALUE = "Value";
    private String viewType;
    private String selectedValue;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mQuotesReference;
    private ValueEventListener quotesListener_Tag;
    private ValueEventListener quotesListener_Author;
    private ArrayList<Quote> quotesList = new ArrayList<>();
    private String FRAGMENT_TAG = "QUOTES_LIST_FRAGMENT";

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.quotes_toolbar)
    Toolbar toolbar;

    @BindView(R.id.quotes_image)
    ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotes_list_activity);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        //Downloaded the image "http://www.ultimatedesignertoolkit.com/wp-content/uploads/2015/11/Background_1.jpg" for use as background
        backgroundImage.setImageResource(R.drawable.background);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle(getString(R.string.app_title_quotes));

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            FirebaseCrash.report(new Exception("QuotesListActivity initialized without the needed values"));
        }
        else {
            viewType = extras.getString(KEY_VIEW_TYPE);
            selectedValue = extras.getString(KEY_SELECTED_VALUE);
            //Initialize firebase database reference and fetch the quotes
            setUpFirebase();
            fetchQuotes();
        }
    }

    private void fetchQuotes() {
        if("categories".equals(viewType)){
            quotesListener_Tag = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    quotesList.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Quote quote = child.getValue(Quote.class);
                        quotesList.add(quote);
                    }
                    initializeFragment();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    FirebaseCrash.log("fetchQuotes - Could not fetch data : "+databaseError.toString());
                }
            };
            mQuotesReference.orderByChild("tag").equalTo(selectedValue).addValueEventListener(quotesListener_Tag);
        }
        else if("authors".equals(viewType)){
            quotesListener_Author = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    quotesList.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Quote quote = child.getValue(Quote.class);
                        quotesList.add(quote);
                    }
                    initializeFragment();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    FirebaseCrash.log("fetchQuotes - Could not fetch data : "+databaseError.toString());
                }
            };
            mQuotesReference.orderByChild("author").equalTo(selectedValue).addValueEventListener(quotesListener_Author);
        }
    }

    /**
     * Attach the fragment
     */
    private void initializeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        QuotesListFragment quotesListFragment = new QuotesListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("QUOTES_LIST", quotesList);
        quotesListFragment.setArguments(bundle);

        ft.replace(R.id.quotes_list_fragment_holder, quotesListFragment, FRAGMENT_TAG).commit();
    }

    private void setUpFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mQuotesReference = mFirebaseDatabase.getReference("quotes");
        mQuotesReference.keepSynced(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mQuotesReference != null && quotesListener_Tag != null) {
            mQuotesReference.removeEventListener(quotesListener_Tag);
        }
        if (mQuotesReference != null && quotesListener_Author != null) {
            mQuotesReference.removeEventListener(quotesListener_Author);
        }
    }
}
