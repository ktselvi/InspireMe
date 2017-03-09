package com.ktselvi.inspireme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.ktselvi.inspireme.fragments.FavouritesFragment;
import com.ktselvi.inspireme.handlers.FavQuoteClickListener;
import com.ktselvi.inspireme.model.Quote;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tkumares on 07-Mar-17.
 */

public class FavQuotesActivity extends AppCompatActivity implements FavQuoteClickListener {

    private static final String FRAGMENT_TAG = "FavouritesFragment";
    @BindView(R.id.fav_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_quotes_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize the fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //If fragment is not already present, then attach it
        if(fm.findFragmentByTag(FRAGMENT_TAG) == null){
            FavouritesFragment quotesListFragment = new FavouritesFragment();
            ft.replace(R.id.fav_quotes_list_fragment_holder, quotesListFragment, FRAGMENT_TAG).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleFavQuoteClick(int position, ArrayList<Quote> quotes) {
        Intent intent = new Intent(this, QuoteDetailActivity.class);
        intent.putExtra("QUOTE_INDEX", position);
        intent.putParcelableArrayListExtra("QUOTES_LIST", quotes);
        //Because we just want to display the detailed quote to the user in a read only mode way, we won't display FAB
        intent.putExtra("DISPLAY_FAB", false);
        startActivity(intent);
    }
}
