package com.ktselvi.inspireme;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private ArrayList<Quote> quotes;
    private boolean isFav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        int currentPosition = intent.getIntExtra("QUOTE_INDEX", 0);
        quotes = intent.getParcelableArrayListExtra("QUOTES_LIST");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_title_quote));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting the pager adapter
        PagerAdapter pagerAdapter = new QuoteDetailPagerAdapter(getSupportFragmentManager(), quotes);
        quoteDetailPager.setAdapter(pagerAdapter);
        quoteDetailPager.setCurrentItem(currentPosition);

        //set the properties of FAB
        setFAB();
    }

    private void setFAB() {
        //Query the DB to see if this quote is marked as favourite
        //If yes, then the FAB will be red colored, else pink

        if(isFav){
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_background_fav)));
        }

        final Context context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFav){
                    Toast.makeText(context, getString(R.string.fav_removed), Toast.LENGTH_SHORT).show();
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    isFav = false;
                }
                else{
                    Toast.makeText(context, getString(R.string.fav_added), Toast.LENGTH_SHORT).show();
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_background_fav)));
                    isFav = true;
                }
            }
        });
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
}
