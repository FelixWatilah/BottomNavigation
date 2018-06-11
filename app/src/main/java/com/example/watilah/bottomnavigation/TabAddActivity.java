package com.example.watilah.bottomnavigation;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class TabAddActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_add);

        Toolbar toolbar = findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.new_recipe);

        // Add back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Remove shadow from the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        tabLayout = findViewById(R.id.tabs_add);
        viewPager = findViewById(R.id.viewpager_add);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments
        adapter.AddFragment(new DescriptionAddFragment(), "Description");
        adapter.AddFragment(new IngredientAddFragment(), "Ingredients");
        adapter.AddFragment(new DirectionAddFragment(), "Directions");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}