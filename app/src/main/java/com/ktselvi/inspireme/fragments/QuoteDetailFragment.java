package com.ktselvi.inspireme.fragments;

import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.database.QuotesContract;
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

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Quote currentQuote;
    private boolean showFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.quote_detail_fragment, container, false);

        ButterKnife.bind(this, v);

        currentQuote = getArguments().getParcelable("KEY_QUOTE_OBJ");
        showFab = getArguments().getBoolean("KEY_FAB_DISPLAY");
        quoteTextView.setText(currentQuote.getQuote());
        authorView.setText(currentQuote.getAuthor());

        //This decides whether to display the FAB or not
        if(showFab){
            //set the properties of FAB
            setFAB();
        }
        else{
            fab.setVisibility(View.GONE);
        }
        return v;
    }

    private void setFAB() {
        //Query the DB to see if this quote is marked as favourite
        //If yes, then the FAB will be red colored, else pink
        if(isQuoteFav()){
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_background_fav)));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick();
            }
        });
    }

    /**
     * Method to check if the quote is already marked as favourite
     * @return boolean result
     */
    private boolean isQuoteFav() {
        Cursor c = this.getActivity().getContentResolver().query(QuotesContract.QuotesTable.CONTENT_URI, null, null, null, null);
        if (c.moveToFirst()){
            while(!c.isAfterLast()){
                String data = c.getString(c.getColumnIndex(QuotesContract.QuotesTable.COLUMN_QUOTE));
                if(currentQuote.getQuote().equals(data)){
                    return true; //Match found
                }
                c.moveToNext();
            }
        }
        c.close();
        return false; //No match found
    }

    /**
     * Click listener for the FAB.
     * If the quote is already a favourite, then it will remove it from favourites
     * If not present in DB, it will add to the DB as favourite
     */
    private void handleClick(){
        int quoteId = getQuoteId();
        if(quoteId != -1){
            this.getActivity().getContentResolver().delete(Uri.parse(QuotesContract.QuotesTable.CONTENT_URI+"/"+quoteId),null,null);
            Toast.makeText(this.getActivity(), this.getActivity().getString(R.string.fav_removed), Toast.LENGTH_SHORT).show();
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
        else{
            ContentValues values = new ContentValues();
            values.put(QuotesContract.QuotesTable.COLUMN_QUOTE, currentQuote.getQuote());
            values.put(QuotesContract.QuotesTable.COLUMN_AUTHOR, currentQuote.getAuthor());
            values.put(QuotesContract.QuotesTable.COLUMN_TAG, currentQuote.getTag());
            this.getActivity().getContentResolver().insert(QuotesContract.QuotesTable.CONTENT_URI, values);
            Toast.makeText(this.getActivity(), this.getActivity().getString(R.string.fav_added), Toast.LENGTH_SHORT).show();
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_background_fav)));
        }
    }

    /**
     * Method to get the id of the quote record if it exists. It will return -1 if the quote is not yet marked as favourite
     * @return int id
     */
    private int getQuoteId() {
        Cursor c = this.getActivity().getContentResolver().query(QuotesContract.QuotesTable.CONTENT_URI, null, null, null, null);
        if (c.moveToFirst()){
            while(!c.isAfterLast()){
                String data = c.getString(c.getColumnIndex(QuotesContract.QuotesTable.COLUMN_QUOTE));
                if(currentQuote.getQuote().equals(data)){
                    return c.getInt(c.getColumnIndex(QuotesContract.QuotesTable.COLUMN_ID)); //Match found
                }
                c.moveToNext();
            }
        }
        c.close();
        return -1; //No match found
    }
}
