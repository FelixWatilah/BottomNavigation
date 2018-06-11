package com.example.watilah.bottomnavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterNutrition extends RecyclerView.Adapter<RecyclerViewAdapterNutrition.MyViewHolder> {

    private Context mContext;
    private List<Nutrition> mData;

    public RecyclerViewAdapterNutrition(Context mContext, List<Nutrition> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        view = layoutInflater.inflate(R.layout.list_item_nutrition, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_nutrition.setText(mData.get(position).getNutrition());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_counter, tv_nutrition;

        public MyViewHolder(View itemView) {
            super(itemView);

            //tv_counter = itemView.findViewById(R.id.lin_counter);
            tv_nutrition = itemView.findViewById(R.id.lin_nutrition);

        }
    }

}
