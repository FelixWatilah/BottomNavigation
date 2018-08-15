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
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Users> listUsers;

    UsersRecyclerViewAdapter(Context context, List<Users> listUsers) {
        this.context = context;
        this.listUsers = listUsers;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, email, usertype;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            usertype = itemView.findViewById(R.id.usertype);

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.name.setText(listUsers.get(position).getName());
        holder.email.setText(listUsers.get(position).getEmail());

        if (Integer.parseInt(listUsers.get(position).getUsertype()) == 1){
            holder.usertype.setText("Admin");
        }else{
            holder.usertype.setText("Chef");
        }

    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public void setFilter(List<Users> usersList) {
        this.listUsers = new ArrayList<>();
        this.listUsers.addAll(usersList);
        notifyDataSetChanged();
    }

}