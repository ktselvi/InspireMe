package com.ktselvi.inspireme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.adapters.CategoryAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class CategoriesListFragment extends Fragment {

    private ArrayList<String> categories;
    private LinearLayoutManager layoutManager;
    private CategoryAdapter adapter;

    @BindView(R.id.categories_recycler_view)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Used a recycler view layout with card view for displaying categories
        View v = inflater.inflate(R.layout.categories_list_fragment, container, false);
        categories = getArguments().getStringArrayList("CAT_LIST");
        ButterKnife.bind(this, v);

        //Set the layout manager for the recycler view
        layoutManager = new LinearLayoutManager(getContext());;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if(categories != null){
            //set the adapter
            adapter = new CategoryAdapter(categories);
            recyclerView.setAdapter(adapter);
        }

        return v;
    }
}
