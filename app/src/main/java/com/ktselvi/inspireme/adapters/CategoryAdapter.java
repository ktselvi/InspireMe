package com.ktselvi.inspireme.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktselvi.inspireme.R;

import java.util.ArrayList;

/**
 * Created by tkumares on 05-Mar-17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private ArrayList<String> dataList;

    public CategoryAdapter(ArrayList<String> data){
        dataList = new ArrayList<>(data);
    }

    @Override
    public CategoryAdapter.CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_card_layout, parent, false);
        CategoryHolder myViewHolder = new CategoryHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.CategoryHolder holder, int position) {
        TextView textView = holder.categoryName;
        textView.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        public CategoryHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
        }
    }
}
