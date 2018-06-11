package com.example.watilah.bottomnavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class RecyclerViewAdapterIngredient extends RecyclerView.Adapter<RecyclerViewAdapterIngredient.MyViewHolder> {

    private Context mContext;
    private List<Ingredient> mData;

    public RecyclerViewAdapterIngredient(Context mContext, List<Ingredient> mData) {

        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.list_item_ingredient, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.warning(mContext, "Test Click" + String.valueOf(myViewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //holder.tv_counter.setText(mData.get(position).getCounter());
        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_qnty.setText(mData.get(position).getQnty());
        holder.iv_Image.setImageResource(mData.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_counter, tv_name, tv_qnty;
        ImageView iv_Image;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayoutIngredient);
            //tv_counter = itemView.findViewById(R.id.lii_counter);
            tv_name = itemView.findViewById(R.id.lii_name);
            tv_qnty = itemView.findViewById(R.id.lii_qnty);
            iv_Image = itemView.findViewById(R.id.lii_img);
        }
    }

}
