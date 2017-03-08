package com.ktselvi.inspireme.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.database.QuotesContract;
import com.ktselvi.inspireme.handlers.FavQuoteClickListener;
import com.ktselvi.inspireme.model.Quote;

import java.util.ArrayList;

/**
 * Created by tkumares on 07-Mar-17.
 */

public class FavQuotesAdapter extends RecyclerView.Adapter<FavQuotesAdapter.FavQuoteHolder> {

    private Cursor cursor;
    private Context mContext;

    public FavQuotesAdapter(Context context, Cursor c){
        cursor = c;
        mContext = context;
    }


    @Override
    public FavQuotesAdapter.FavQuoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_quotes_list_item, parent, false);
        FavQuoteHolder viewHolder = new FavQuoteHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FavQuotesAdapter.FavQuoteHolder holder, final int position) {
        cursor.moveToPosition(position);
        if(cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesTable.COLUMN_ID)) != null){
            holder.quoteView.setText(cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesTable.COLUMN_QUOTE)));
            holder.authorView.setText(mContext.getString(R.string.author_label)+cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesTable.COLUMN_AUTHOR)));
            holder.tagView.setText(mContext.getString(R.string.tag_label)+cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesTable.COLUMN_TAG)));

            holder.quoteView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    ArrayList<Quote> quotes = new ArrayList<Quote>();
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        Quote q = new Quote();
                        q.setQuote(cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesTable.COLUMN_QUOTE)));
                        q.setAuthor(cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesTable.COLUMN_AUTHOR)));
                        q.setTag(cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesTable.COLUMN_TAG)));
                        quotes.add(q);
                        cursor.moveToNext();
                    }
                    ((FavQuoteClickListener)mContext).handleFavQuoteClick(position,quotes);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public class FavQuoteHolder extends RecyclerView.ViewHolder {
        TextView quoteView;
        TextView authorView;
        TextView tagView;
        public FavQuoteHolder(View itemView) {
            super(itemView);
            quoteView = (TextView) itemView.findViewById(R.id.fav_quote_text);
            authorView = (TextView) itemView.findViewById(R.id.fav_quote_author);
            tagView = (TextView) itemView.findViewById(R.id.fav_quote_tag);
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (cursor == newCursor) {
            return null;
        }
        Cursor oldCursor = cursor;
        cursor = newCursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }
}