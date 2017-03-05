package com.ktselvi.inspireme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ktselvi.inspireme.model.Quote;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuoteDetailActivity extends AppCompatActivity {

    @BindView(R.id.quote_detail_text)
    TextView quoteTextHolder;

    @BindView(R.id.quote_detail_author_name)
    TextView authorHolder;

    @BindView(R.id.quote_detail_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Quote quote = intent.getParcelableExtra("QUOTE_OBJECT");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_title_quote));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quoteTextHolder.setText(quote.getQuote());
        authorHolder.setText(quote.getAuthor());

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
