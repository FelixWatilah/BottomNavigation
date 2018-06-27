package com.example.watilah.bottomnavigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterRecipe extends RecyclerView.Adapter<RecyclerViewAdapterRecipe.MyViewHolder> {

    private RequestOptions options;
    private Context mContext;
    private List<Recipe> mData;

    RecyclerViewAdapterRecipe(Context mContext, List<Recipe> mData) {
        this.mContext = mContext;
        this.mData = mData;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_refresh_black_24dp)
                .error(R.drawable.ic_error_outline_white_48dp);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        view = layoutInflater.inflate(R.layout.recipe_row_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_food_name.setText(mData.get(position).getName());
        holder.tv_food_category.setText(mData.get(position).getCategory());
        holder.tv_food_desc.setText(mData.get(position).getDescription());

        // Load Image with glide
        Glide.with(mContext).load(mData.get(position).getThumbnail()).apply(options).into(holder.img_food_thumbnail);

        // Set OnclickListener
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, DetailsActivity.class);

                intent.putExtra("Image", mData.get(position).getThumbnail());
                intent.putExtra("Title", mData.get(position).getName());
                intent.putExtra("Category",mData.get(position).getCategory());
                intent.putExtra("Description",mData.get(position).getDescription());

                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setFilter(List<Recipe> recipeList) {
        mData = new ArrayList<>();
        mData.addAll(recipeList);
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_food_name, tv_food_desc, tv_food_category;
        ImageView img_food_thumbnail;
        LinearLayout linearLayout;

        MyViewHolder(View itemView) {
            super(itemView);

            tv_food_name = itemView.findViewById(R.id.food_name);
            tv_food_desc = itemView.findViewById(R.id.food_desc);
            tv_food_category = itemView.findViewById(R.id.food_category);
            img_food_thumbnail = itemView.findViewById(R.id.food_image);
            linearLayout = itemView.findViewById(R.id.linear_layout);

        }
    }

}