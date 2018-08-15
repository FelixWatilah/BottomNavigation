package com.example.watilah.bottomnavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DeletedRecyclerViewAdapter extends RecyclerView.Adapter<DeletedRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<DeletedRecipe> listDeletedRecipe;

    DeletedRecyclerViewAdapter(Context context, List<DeletedRecipe> listDeletedRecipe) {
        this.context = context;
        this.listDeletedRecipe = listDeletedRecipe;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_food_name, tv_food_desc, tv_food_category;
        public ImageView img_food_thumbnail;
        public FrameLayout linearLayout;
        public RelativeLayout viewBackground, viewForeground;
        public Button restoreButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_food_name = itemView.findViewById(R.id.food_name);
            tv_food_desc = itemView.findViewById(R.id.food_desc);
            tv_food_category = itemView.findViewById(R.id.food_category);
            img_food_thumbnail = itemView.findViewById(R.id.food_image);
            linearLayout = itemView.findViewById(R.id.frame_layout);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            viewBackground = itemView.findViewById(R.id.view_background);
            //restoreButton = itemView.findViewById(R.id.btn_restore);

        }


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.deleted_row_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final String imgUrl = AppConfig.PATH + "/recipe/uploads/" + listDeletedRecipe.get(position).getThumbnail() + ".jpg";

        holder.tv_food_name.setText(listDeletedRecipe.get(position).getName());
        holder.tv_food_category.setText(listDeletedRecipe.get(position).getCategory());
        holder.tv_food_desc.setText(listDeletedRecipe.get(position).getDescription());

        // Load Image with glide
        GlideApp.with(context)
                .load(imgUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_hourglass_black)
                .error(R.drawable.ic_error_outline_white_48dp)
                .transition(DrawableTransitionOptions.withCrossFade(2000))
                .into(holder.img_food_thumbnail);

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toasty.success(context, "OnLongClick called at position" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        /*holder.restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listDeletedRecipe.size();
    }

    public void setFilter(List<DeletedRecipe> recipeDeletedList) {
        this.listDeletedRecipe = new ArrayList<>();
        this.listDeletedRecipe.addAll(recipeDeletedList);
        notifyDataSetChanged();
    }

    /*public void restoreItem(int position, DeletedRecipe deletedRecipe) {
        listDeletedRecipe.add(position, deletedRecipe);
        // notify item added by position
        notifyItemInserted(position);
    }*/

}