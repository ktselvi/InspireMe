package com.ktselvi.inspireme.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.adapters.FavQuotesAdapter;
import com.ktselvi.inspireme.database.QuotesContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tkumares on 07-Mar-17.
 */

public class FavouritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Cursor mCursor;
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private FavQuotesAdapter mAdapter;
    private static final int LOADER_ID = 1;

    @BindView(R.id.fav_quotes_recycler_view)
    RecyclerView favRecyclerView;

    @BindView(R.id.no_data_textview)
    TextView noFavText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Used a recycler view layout with card view for displaying categories
        View v = inflater.inflate(R.layout.fav_quotes_fragment, container, false);
        ButterKnife.bind(this, v);

        //Initialize loader
        mCallbacks = this;
        getLoaderManager().initLoader(LOADER_ID, null, mCallbacks);

        //Initialize recyclerview adapter
        mAdapter = new FavQuotesAdapter(getActivity(),mCursor);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        favRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this.getActivity(), QuotesContract.QuotesTable.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.getCount() == 0){
            noFavText.setVisibility(View.VISIBLE);
            favRecyclerView.setVisibility(View.GONE);
        }
        else{
            noFavText.setVisibility(View.GONE);
            favRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
