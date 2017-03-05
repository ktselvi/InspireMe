package com.ktselvi.inspireme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.handlers.QuoteClickListener;
import com.ktselvi.inspireme.model.Quote;

import java.util.ArrayList;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QuoteHolder>  {

    private ArrayList<Quote> quotesList;
    private String viewType;
    private Context context;

    public QuotesAdapter(Context context, ArrayList<Quote> quotesList, String viewType) {
        this.context = context;
        this.quotesList = quotesList;
        this.viewType = viewType;
    }

    @Override
    public void onBindViewHolder(QuotesAdapter.QuoteHolder holder, int position) {
        TextView quoteTextView = holder.quoteView;
        TextView infoTextView = holder.infoView;

        final Quote quoteObj = quotesList.get(position);
        quoteTextView.setText(quoteObj.getQuote());

        int sizeInDp = 10;
        float scale = context.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);

        //Using the viewType here to determine if the quotes are for a particular category or an author
        //If the quotes are with respect to an author, display the tag associated with the quote
        //If the quotes are with respect to a particular category, then display the author name for each quote
        //Also, tag info will be displayed at the beginning(left hand side), whereas author name will be displayed at the end(right hand side)
        if("categories".equals(this.viewType)){
            infoTextView.setText(quoteObj.getAuthor());
            infoTextView.setGravity(Gravity.END);
            infoTextView.setPaddingRelative(0,dpAsPixels,dpAsPixels,dpAsPixels);
        }
        else if("authors".equals(this.viewType)){
            infoTextView.setText("Tag: "+quoteObj.getTag());
            infoTextView.setGravity(Gravity.START);
            infoTextView.setPaddingRelative(dpAsPixels,dpAsPixels,0,dpAsPixels);
        }

        final int clickedPosition = position;

        quoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof QuoteClickListener){
                    ((QuoteClickListener)context).handleQuoteClicked(clickedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return quotesList.size();
    }

    public class QuoteHolder extends RecyclerView.ViewHolder {
        TextView quoteView;
        TextView infoView;

        public QuoteHolder(View itemView) {
            super(itemView);
            quoteView = (TextView) itemView.findViewById(R.id.quote_text);
            infoView = (TextView) itemView.findViewById(R.id.quote_info);
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
