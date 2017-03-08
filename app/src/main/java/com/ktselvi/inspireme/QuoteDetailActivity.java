package com.ktselvi.inspireme;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ktselvi.inspireme.adapters.QuoteDetailPagerAdapter;
import com.ktselvi.inspireme.model.Quote;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuoteDetailActivity extends AppCompatActivity {

    @BindView(R.id.quote_pager)
    ViewPager quoteDetailPager;

    @BindView(R.id.quote_detail_toolbar)
    Toolbar toolbar;

    private ArrayList<Quote> quotes;
    boolean showFab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);

        ButterKnife.bind(this);
        int currentPosition = 0;

        if(savedInstanceState != null){
            currentPosition = savedInstanceState.getInt("KEY_POSITION");
            quotes = savedInstanceState.getParcelableArrayList("KEY_LIST");
            showFab = savedInstanceState.getBoolean("KEY_FAB");
        }
        else {
            Intent intent = getIntent();
            currentPosition = intent.getIntExtra("QUOTE_INDEX", 0);
            quotes = intent.getParcelableArrayListExtra("QUOTES_LIST");
            showFab = intent.getBooleanExtra("DISPLAY_FAB", true);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_title_quote));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting the pager adapter with the data and the flag to display FAB
        PagerAdapter pagerAdapter = new QuoteDetailPagerAdapter(getSupportFragmentManager(), quotes, showFab);
        quoteDetailPager.setAdapter(pagerAdapter);
        quoteDetailPager.setCurrentItem(currentPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                int i = quoteDetailPager.getCurrentItem();
                String quoteToBeShared = getString(R.string.share_quote_subject)+ " "+ quotes.get(i).getQuote()+ " - "+ quotes.get(i).getAuthor();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_quote_subject));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, quoteToBeShared);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //Save the state variables
        savedInstanceState.putInt("KEY_POSITION", quoteDetailPager.getCurrentItem());
        savedInstanceState.putParcelableArrayList("KEY_LIST", quotes);
        savedInstanceState.putBoolean("KEY_FAB", showFab);
    }
}
