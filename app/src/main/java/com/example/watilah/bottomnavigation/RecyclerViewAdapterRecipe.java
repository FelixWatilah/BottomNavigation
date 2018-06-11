package com.example.watilah.bottomnavigation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class RecyclerViewAdapterRecipe extends RecyclerView.Adapter<RecyclerViewAdapterRecipe.MyViewHolder> {

    RequestOptions options;
    private Context mContext;
    private List<Recipe> mData;

    public RecyclerViewAdapterRecipe(Context mContext, List<Recipe> mData) {
        this.mContext = mContext;
        this.mData = mData;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_refresh_black_24dp)
                .error(R.drawable.ic_error_outline_white_48dp);
    }

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
        holder.tv_food_desc.setText(mData.get(position).getDescription());

        // Load Image with glide
        Glide.with(mContext).load(mData.get(position).getThumbnail()).apply(options).into(holder.img_food_thumbnail);

        // Set OnclickListener
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, TabLayoutActivity.class);

                intent.putExtra("Image", mData.get(position).getThumbnail());
                intent.putExtra("Title", mData.get(position).getName());

                mContext.startActivity(intent);

            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toasty.success(mContext, "Test Click" + String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_food_name, tv_food_desc;
        ImageView img_food_thumbnail, favorite;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_food_name = itemView.findViewById(R.id.food_name);
            tv_food_desc = itemView.findViewById(R.id.food_desc);
            img_food_thumbnail = itemView.findViewById(R.id.food_image);
            favorite = itemView.findViewById(R.id.favorite);
            linearLayout = itemView.findViewById(R.id.linear_layout);

        }
    }

}