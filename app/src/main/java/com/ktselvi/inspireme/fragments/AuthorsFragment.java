package com.ktselvi.inspireme.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.crash.FirebaseCrash;
import com.ktselvi.inspireme.MainActivity;
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

        int orientation = getResources().getConfiguration().orientation;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        //Checking the device configuration to figure out the correct layout to be used
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            layoutManager = new GridLayoutManager(getContext(),2);
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE && dpWidth >= 600)
            layoutManager = new GridLayoutManager(this.getActivity(), 3);
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE && dpWidth < 600)
            layoutManager = new GridLayoutManager(this.getActivity(), 2);

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

    /**
     * When the fragment is resumed, want to set the correct selection in the navigation drawer
     */
    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity)this.getActivity()).setNavDrawerCheckedItem(R.id.nav_authors);
    }
}
