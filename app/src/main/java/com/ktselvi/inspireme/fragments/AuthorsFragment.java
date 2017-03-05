package com.ktselvi.inspireme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.crash.FirebaseCrash;
import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.adapters.AuthorAdapter;
import com.ktselvi.inspireme.adapters.CategoryAdapter;
import com.ktselvi.inspireme.model.Author;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class AuthorsFragment extends Fragment {

    private ArrayList<Author> authors;
    private GridLayoutManager layoutManager;
    private AuthorAdapter adapter;

    @BindView(R.id.author_recycler_view)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Used a recycler view layout with card view for displaying categories
        View v = inflater.inflate(R.layout.authors_list_fragment, container, false);
        authors = getArguments().getParcelableArrayList("AUTHORS_LIST");
        ButterKnife.bind(this, v);

        //Set the layout manager for the recycler view
        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if(authors != null){
            //set the adapter
            adapter = new AuthorAdapter(getActivity(), authors);
            recyclerView.setAdapter(adapter);
        }
        else {
            FirebaseCrash.log("AuthorsFragment invoked without data");
        }

        return v;
    }
}
