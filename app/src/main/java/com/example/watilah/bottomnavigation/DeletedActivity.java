package com.example.watilah.bottomnavigation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.watilah.bottomnavigation.helper.SQLiteHandler;
import com.example.watilah.bottomnavigation.helper.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeletedActivity extends AppCompatActivity {

    private SearchView searchView;
    private List<DeletedRecipe> listDeletedRecipe;
    private RecyclerView recyclerView;
    private DeletedRecyclerViewAdapter deletedRecyclerViewAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    private Toolbar toolbarDeleted;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted);

        toolbarDeleted = findViewById(R.id.toolbar_deleted);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.deleted_recycler_view);
        listDeletedRecipe = new ArrayList<>();
        deletedRecyclerViewAdapter = new DeletedRecyclerViewAdapter(getApplicationContext(), listDeletedRecipe);


        setSupportActionBar(toolbarDeleted);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Deleted Recipes");
        toolbarDeleted.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(deletedRecyclerViewAdapter);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        jsonCall();

    }

    public void jsonCall() {

        StringRequest arrayRequest = new StringRequest(Request.Method.GET, AppConfig.URL_DELETED_RECIPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Couldn't fetch the recipes! Pleas try again.", Toast.LENGTH_LONG).show();
                    return;
                }

                try {

                    JSONArray array = new JSONArray(response);

                    //listMainRecipe.clear();

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);

                        listDeletedRecipe.add(new DeletedRecipe(
                                jsonObject.getString("name"),
                                jsonObject.getString("category"),
                                jsonObject.getString("description"),
                                jsonObject.getString("image")

                        ));

                        // refreshing recycler view
                        deletedRecyclerViewAdapter.notifyDataSetChanged();

                        // stop animating Shimmer and hide the layout
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(arrayRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutUser();
                break;
            case android.R.id.home:
                finish();
                break;
        }


        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {


        getMenuInflater().inflate(R.menu.menu_search, menu);
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        final MenuItem menuItem = menu.findItem(R.id.search_view);

        searchView = (SearchView) menuItem.getActionView();

        changeSearchViewTextColor(searchView);

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
                final List<DeletedRecipe> filterModelList = filter(listDeletedRecipe, newText);
                deletedRecyclerViewAdapter.setFilter(filterModelList);
                return true;
            }
        });

        return true;
    }

    private List<DeletedRecipe> filter(List<DeletedRecipe> pl, String query) {
        query = query.toLowerCase();
        final List<DeletedRecipe> filteredModeList = new ArrayList<>();

        for (DeletedRecipe model : pl) {

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

    private void logoutUser() {
        session.setLogin(false,2);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
