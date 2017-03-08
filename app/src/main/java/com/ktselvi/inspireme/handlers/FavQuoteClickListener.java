package com.ktselvi.inspireme.handlers;

import com.ktselvi.inspireme.model.Quote;

import java.util.ArrayList;

/**
 * Created by tkumares on 08-Mar-17.
 */

public interface FavQuoteClickListener {
    void handleFavQuoteClick(int position, ArrayList<Quote> quotes);
}

