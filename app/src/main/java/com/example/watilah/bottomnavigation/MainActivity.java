package com.example.watilah.bottomnavigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.rom4ek.arcnavigationview.ArcNavigationView;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity implements ArcNavigationView.OnNavigationItemSelectedListener {

    /*SearchView searchView;
    List<Recipe> listRecipe = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapterRecipe recyclerViewAdapterRecipe;*/
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ArcNavigationView arcNavigationView;
    //private ShimmerFrameLayout mShimmerViewContainer;

    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Recipe");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setElevation(0);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ArcNavigationView arcNavigationView = findViewById(R.id.nav_view);
        arcNavigationView.setNavigationItemSelectedListener(this);

        FabSpeedDial fabSpeedDial = findViewById(R.id.fab_more);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {

                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                Toast.makeText(getApplicationContext(), "" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();

                return true;
            }

            @Override
            public void onMenuClosed() {
                super.onMenuClosed();
            }
        });


        /*recyclerView = findViewById(R.id.recycler_view);
        recyclerViewAdapterRecipe = new RecyclerViewAdapterRecipe(this, listRecipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapterRecipe);*/

        // Call json method
        //jsonCall();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeFragment()).commit();
            arcNavigationView.setCheckedItem(R.id.saved_recipes);
        }

    }

    /*public void jsonCall() {

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
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /*@Override
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
    }*/

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.new_recipe:
                // Create new fragment and transaction
                AddFragment addFragment = new AddFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                fragmentTransaction.replace(R.id.fragment_container, addFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.saved_recipes:
                // Create new fragment and transaction
                RecipeFragment recipeFragment = new RecipeFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                fragmentTransaction.replace(R.id.fragment_container, recipeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.shopping_list:
                // Create new fragment and transaction
                ShoppingFragment shoppingFragment = new ShoppingFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                fragmentTransaction.replace(R.id.fragment_container, shoppingFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.share:
                Toasty.success(this, "Shared", Toast.LENGTH_SHORT).show();
                break;
        }

        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

}