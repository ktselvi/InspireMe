package com.ktselvi.inspireme.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.crash.FirebaseCrash;
import com.ktselvi.inspireme.MainActivity;
import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.adapters.CategoryAdapter;
import com.ktselvi.inspireme.handlers.CategoryClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class CategoriesListFragment extends Fragment {

    private ArrayList<String> categories;
    private RecyclerView.LayoutManager layoutManager;
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

        int orientation = getResources().getConfiguration().orientation;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        //Checking the device configuration to figure out the correct layout to be used
        if (orientation == Configuration.ORIENTATION_PORTRAIT && dpWidth < 600)
            layoutManager = new LinearLayoutManager(getContext());
        else if (orientation == Configuration.ORIENTATION_PORTRAIT && dpWidth >= 600)
            layoutManager = new GridLayoutManager(this.getActivity(), 2);
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE && dpWidth >= 600)
            layoutManager = new GridLayoutManager(this.getActivity(), 3);
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE && dpWidth < 600)
            layoutManager = new GridLayoutManager(this.getActivity(), 2);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if(categories != null){
            //set the adapter
            adapter = new CategoryAdapter((CategoryClickListener) getActivity(), categories);
            recyclerView.setAdapter(adapter);
        }
        else {
            FirebaseCrash.log("CategoriesListFragment invoked without data");
        }

        return v;
    }

    /**
     * When the fragment is resumed, want to set the correct selection in the navigation drawer
     */
    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity)this.getActivity()).setNavDrawerCheckedItem(R.id.nav_category);
    }
}
