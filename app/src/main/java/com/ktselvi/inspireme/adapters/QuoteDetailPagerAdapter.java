package com.ktselvi.inspireme.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ktselvi.inspireme.fragments.QuoteDetailFragment;
import com.ktselvi.inspireme.model.Quote;

import java.util.ArrayList;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class QuoteDetailPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Quote> quotesList;

    public QuoteDetailPagerAdapter(FragmentManager fm, ArrayList<Quote> data) {
        super(fm);
        this.quotesList = data;
    }

    @Override
    public Fragment getItem(int position) {
        QuoteDetailFragment quoteFragment = new QuoteDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("KEY_QUOTE_OBJ", quotesList.get(position));
        quoteFragment.setArguments(bundle);
        return quoteFragment;
    }

    @Override
    public int getCount() {
        return quotesList.size();
    }
}
