package com.example.watilah.bottomnavigation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    List<Recipe> listRecipe = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapterRecipe recyclerViewAdapterRecipe;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private ShimmerFrameLayout mShimmerViewContainer;

    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Recipe");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ArcNavigationView arcNavigationView = findViewById(R.id.nav_view);
        arcNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.new_recipe) {
                    Intent toAddActivity = new Intent(getApplicationContext(), AddActivity.class);
                    startActivity(toAddActivity);
                    return true;
                }

                if (id == R.id.saved_recipes) {
                    Toast.makeText(getApplicationContext(), "Recipes", Toast.LENGTH_SHORT).show();
                    return true;
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewAdapterRecipe = new RecyclerViewAdapterRecipe(this, listRecipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapterRecipe);

        // Call json method
        jsonCall();

    }

    public void jsonCall() {

        String URL_JSON = "https://gist.githubusercontent.com/aws1994/f583d54e5af8e56173492d3f60dd5ebf/raw/c7796ba51d5a0d37fc756cf0fd14e54434c547bc/anime.json";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject;

                for (int i = 0; i < response.length(); i++) {

                    //Toast.makeText(getApplicationContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();

                    try {

                        jsonObject = response.getJSONObject(i);
                        Recipe recipe = new Recipe();

                        recipe.setName(jsonObject.getString("name"));
                        recipe.setCategory(jsonObject.getString("categorie"));
                        recipe.setDescription(jsonObject.getString("description"));
                        recipe.setThumbnail(jsonObject.getString("img"));

                        //Toast.makeText(MainActivity.this,recipe.toString(),Toast.LENGTH_SHORT).show();

                        listRecipe.add(recipe);

                        // refreshing recycler view
                        recyclerViewAdapterRecipe.notifyDataSetChanged();

                        // stop animating Shimmer and hide the layout
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(MainActivity.this, "Size of List" + String.valueOf(listRecipe.size()), Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, listRecipe.get(1).toString(), Toast.LENGTH_SHORT).show();

                //setRvAdapter(listRecipe);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(arrayRequest);
    }

    /*public void setRvAdapter(List<Recipe> listRecipe) {

        recyclerViewAdapterRecipe = new RecyclerViewAdapterRecipe(this, listRecipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapterRecipe);

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem menuItem = menu.findItem(R.id.search);

        searchView = (SearchView) menuItem.getActionView();

        changeSearchViewTextColor(searchView);

        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.colorIcons));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                menuItem.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Recipe> filterModelList = filter(listRecipe, newText);
                recyclerViewAdapterRecipe.setFilter(filterModelList);
                return true;
            }
        });

        return true;
    }

    private List<Recipe> filter(List<Recipe> pl, String query) {
        query = query.toLowerCase();
        final List<Recipe> filteredModeList = new ArrayList<>();

        for (Recipe model : pl) {

            final String name = model.getName().toLowerCase();
            final String desc = model.getDescription().toLowerCase();

            if (name.contains(query) || desc.contains(query)) {
                filteredModeList.add(model);
            }

        }

        return filteredModeList;

    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {

            if (view instanceof TextView) {

                ((TextView) view).setTextColor(Color.WHITE);

            } else if (view instanceof ViewGroup) {

                ViewGroup viewGroup = (ViewGroup) view;

                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }

            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

}