package com.ktselvi.inspireme.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.model.Quote;

import java.util.ArrayList;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QuoteHolder>  {

    private ArrayList<Quote> quotesList;

    public QuotesAdapter(ArrayList<Quote> quotesList) {
        this.quotesList = quotesList;
    }

    @Override
    public void onBindViewHolder(QuotesAdapter.QuoteHolder holder, int position) {
        TextView quoteTextView = holder.quoteView;
        TextView authorTextView = holder.authorNameView;

        Quote quoteObj = quotesList.get(position);
        quoteTextView.setText(quoteObj.getQuote());
        authorTextView.setText(quoteObj.getAuthor());
    }

    @Override
    public int getItemCount() {
        return quotesList.size();
    }

    public class QuoteHolder extends RecyclerView.ViewHolder {
        TextView quoteView;
        TextView authorNameView;

        public QuoteHolder(View itemView) {
            super(itemView);
            quoteView = (TextView) itemView.findViewById(R.id.quote_text);
            authorNameView = (TextView) itemView.findViewById(R.id.quote_author_name);
        }
    }

    @Override
    public QuotesAdapter.QuoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quote_list_item, parent, false);
        QuotesAdapter.QuoteHolder myViewHolder = new QuotesAdapter.QuoteHolder(view);
        return myViewHolder;
    }

}
