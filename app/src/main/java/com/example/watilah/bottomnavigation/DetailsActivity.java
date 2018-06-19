package com.example.watilah.bottomnavigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = findViewById(R.id.img);

        //Remove shadow from the action bar
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setElevation(0);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing);

        // Receive Data
        Intent intent = getIntent();

        //int Image = intent.getExtras().getInt("Image");
        String Title = Objects.requireNonNull(intent.getExtras()).getString("Title");

        // Receive Bitmap Image from MainActivity
        Bitmap bitmap = intent.getParcelableExtra("Image");

        // Convert Bitmap Image to BitMap Drawable
        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);


        // Setting data
        collapsingToolbarLayout.setTitle(Title);
        imageView.setBackgroundDrawable(ob);

        //collapsingToolbarLayout.setBackgroundDrawable(ob);

    }

}
