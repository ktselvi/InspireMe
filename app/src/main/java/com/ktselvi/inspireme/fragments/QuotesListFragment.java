package com.ktselvi.inspireme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.crash.FirebaseCrash;
import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.adapters.QuotesAdapter;
import com.ktselvi.inspireme.model.Quote;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class QuotesListFragment extends Fragment {

    private ArrayList<Quote> quotesList;

    @BindView(R.id.quotes_recycler_view)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.quotes_list_fragment, container, false);
        ButterKnife.bind(this, v);

        quotesList = getArguments().getParcelableArrayList("QUOTES_LIST");

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if(quotesList != null){
            QuotesAdapter adapter = new QuotesAdapter(quotesList);
            recyclerView.setAdapter(adapter);
        }
        else {
            FirebaseCrash.log("QuotesListFragment invoked without data");
        }

        return v;
    }
}
