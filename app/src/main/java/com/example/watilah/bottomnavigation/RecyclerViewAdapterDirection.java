package com.example.watilah.bottomnavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapterDirection extends RecyclerView.Adapter<RecyclerViewAdapterDirection.MyViewHolder> {

    private Context mContext;
    private List<Direction> mData;

    public RecyclerViewAdapterDirection(Context mContext, List<Direction> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        view = layoutInflater.inflate(R.layout.list_item_direction, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //holder.tv_counter.setText(mData.get(position).getCounter());
        holder.tv_direction.setText(mData.get(position).getDirection());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_counter, tv_direction;

        public MyViewHolder(View itemView) {
            super(itemView);

            //tv_counter = itemView.findViewById(R.id.lid_counter);
            tv_direction = itemView.findViewById(R.id.lid_direction);
        }
    }

}