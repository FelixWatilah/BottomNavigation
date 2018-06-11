package com.example.watilah.bottomnavigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class TabLayoutActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.img);

        //Remove shadow from the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing);

        // Receive Data
        Intent intent = getIntent();

        //int Image = intent.getExtras().getInt("Image");
        String Title = intent.getExtras().getString("Title");

        // Receive Bitmap Image from MainActivity
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("Image");

        // Convert Bitmap Image to BitMap Drawable
        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);


        // Setting data
        collapsingToolbarLayout.setTitle(Title);
        imageView.setBackgroundDrawable(ob);

        //collapsingToolbarLayout.setBackgroundDrawable(ob);

        //Toasty.success(getApplicationContext(), "Loaded", Toast.LENGTH_SHORT).show();

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments
        adapter.AddFragment(new IngredientFragment(), "Ingredients");
        adapter.AddFragment(new DirectionFragment(), "Directions");
        adapter.AddFragment(new NutritionFragment(), "Nutrition");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Add Icons
        /*tabLayout.getTabAt(0).setIcon(R.drawable.ic_cake_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_attach_money_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_control_point_black_24dp);*/

    }

}
