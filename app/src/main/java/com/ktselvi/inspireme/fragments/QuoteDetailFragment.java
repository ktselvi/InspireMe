package com.ktselvi.inspireme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.model.Quote;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class QuoteDetailFragment extends Fragment {

    @BindView(R.id.quote_detail_text)
    TextView quoteTextView;

    @BindView(R.id.quote_detail_author_name)
    TextView authorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.quote_detail_fragment, container, false);

        ButterKnife.bind(this, v);

        Quote currentQuote =getArguments().getParcelable("KEY_QUOTE_OBJ");
        quoteTextView.setText(currentQuote.getQuote());
        authorView.setText(currentQuote.getAuthor());

        return v;
    }
}
