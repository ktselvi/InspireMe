package com.ktselvi.inspireme;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        int currentPosition = intent.getIntExtra("QUOTE_INDEX", 0);
        ArrayList<Quote> quotes = intent.getParcelableArrayListExtra("QUOTES_LIST");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_title_quote));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting the pager adapter
        PagerAdapter pagerAdapter = new QuoteDetailPagerAdapter(getSupportFragmentManager(), quotes);
        quoteDetailPager.setAdapter(pagerAdapter);
        quoteDetailPager.setCurrentItem(currentPosition);

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
}
