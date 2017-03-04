package com.ktselvi.inspireme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.model.Author;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorHolder> {

    private ArrayList<Author> dataList;
    private Context authorContext;

    public AuthorAdapter(Context context, ArrayList<Author> data){
        dataList = new ArrayList<>(data);
        authorContext = context;
    }

    @Override
    public AuthorAdapter.AuthorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.author_view, parent, false);
        AuthorHolder myViewHolder = new AuthorHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AuthorAdapter.AuthorHolder holder, int position) {
        TextView textView = holder.authorName;
        ImageView imgView = holder.profile;
        textView.setText(dataList.get(position).getName());
        Picasso.with(authorContext).load(dataList.get(position).getProfile_picture()).into(imgView);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class AuthorHolder extends RecyclerView.ViewHolder {
        TextView authorName;
        ImageView profile;
        public AuthorHolder(View itemView) {
            super(itemView);
            authorName = (TextView) itemView.findViewById(R.id.author_name);
            profile = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}
