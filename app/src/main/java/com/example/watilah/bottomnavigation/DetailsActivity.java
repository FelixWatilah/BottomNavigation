package com.example.watilah.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    TextView cat, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        cat = findViewById(R.id.details_cat);
        desc = findViewById(R.id.details_desc);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Remove shadow from the action bar
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setElevation(0);

        ImageView imageView = findViewById(R.id.img);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing);

        // Receive Data
        Intent intent = getIntent();

        String Title = Objects.requireNonNull(intent.getExtras()).getString("Title");
        String Image = intent.getExtras().getString("Image");
        String Cat = intent.getExtras().getString("Category");
        String Desc = intent.getExtras().getString("Description");

        // Setting data
        collapsingToolbarLayout.setTitle(Title);

        GlideApp.with(this)
                .load(Image)
                .into(imageView);

        cat.setText(Cat);
        desc.setText(Desc);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
