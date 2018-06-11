package com.example.watilah.bottomnavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapterIngredientAdd extends RecyclerView.Adapter<RecyclerViewAdapterIngredientAdd.MyViewHolder> {

    private Context mContext;
    private List<IngredientAdd> mData;

    public RecyclerViewAdapterIngredientAdd(Context mContext, List<IngredientAdd> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterIngredientAdd.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        view = layoutInflater.inflate(R.layout.list_item_ingredient_add, parent, false);

        return new RecyclerViewAdapterIngredientAdd.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterIngredientAdd.MyViewHolder holder, int position) {

        holder.tv_ingredient.setText(mData.get(position).getIngredientName());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ingredient;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_ingredient = itemView.findViewById(R.id.liia_ingredient);
        }
    }

}