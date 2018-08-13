package com.example.watilah.bottomnavigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterRecipe extends RecyclerView.Adapter<RecyclerViewAdapterRecipe.MyViewHolder> {

    private Context context;
    private List<Recipe> recipeList;

    RecyclerViewAdapterRecipe(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_food_name, tv_food_desc, tv_food_category;
        public ImageView img_food_thumbnail;
        public FrameLayout linearLayout;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_food_name = itemView.findViewById(R.id.food_name);
            tv_food_desc = itemView.findViewById(R.id.food_desc);
            tv_food_category = itemView.findViewById(R.id.food_category);
            img_food_thumbnail = itemView.findViewById(R.id.food_image);
            linearLayout = itemView.findViewById(R.id.frame_layout);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            viewBackground = itemView.findViewById(R.id.view_background);

        }


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.recipe_row_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final String imgUrl = AppConfig.PATH + "/recipe/uploads/" + recipeList.get(position).getThumbnail() + ".jpg";
        final String preptime = recipeList.get(position).getPreptime();
        final String cooktime = recipeList.get(position).getCooktime();
        final String ingredient = recipeList.get(position).getIngredients();
        final String step = recipeList.get(position).getSteps();
        final String comment = recipeList.get(position).getComments();
        final String source = recipeList.get(position).getSource();
        final String url = recipeList.get(position).getUrl();
        final String videourl = recipeList.get(position).getVideourl();

        holder.tv_food_name.setText(recipeList.get(position).getName());
        holder.tv_food_category.setText(recipeList.get(position).getCategory());
        holder.tv_food_desc.setText(recipeList.get(position).getDescription());


        // Load Image with glide
        GlideApp.with(context)
                .load(imgUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_hourglass_black)
                .error(R.drawable.ic_error_outline_white_48dp)
                .transition(DrawableTransitionOptions.withCrossFade(2000))
                .into(holder.img_food_thumbnail);

        // Set OnclickListener
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("Image", imgUrl);
                intent.putExtra("Title", recipeList.get(position).getName());
                intent.putExtra("Category", recipeList.get(position).getCategory());
                intent.putExtra("Description", recipeList.get(position).getDescription());
                intent.putExtra("Preptime", preptime);
                intent.putExtra("Cooktime", cooktime);
                intent.putExtra("Ingredient", ingredient);
                intent.putExtra("Step", step);
                intent.putExtra("Comment", comment);
                intent.putExtra("Source", source);
                intent.putExtra("Url", url);
                intent.putExtra("Videourl", videourl);

                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void removeItem(int position) {
        recipeList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemChanged(position);
    }

    public void restoreItem(Recipe recipe, int position) {
        recipeList.add(position, recipe);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void setFilter(List<Recipe> recipeList) {
        this.recipeList = new ArrayList<>();
        this.recipeList.addAll(recipeList);
        notifyDataSetChanged();
    }


}