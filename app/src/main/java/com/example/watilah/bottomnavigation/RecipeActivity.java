package com.example.watilah.bottomnavigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.watilah.bottomnavigation.helper.RecyclerItemTouchHelper;
import com.example.watilah.bottomnavigation.helper.SQLiteHandler;
import com.example.watilah.bottomnavigation.helper.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private SearchView searchView;
    private List<Recipe> listRecipe;
    private RecyclerView recyclerView;
    private RecipeRecyclerViewAdapter recipeRecyclerViewAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    private Toolbar toolbarRecipe;
    private CoordinatorLayout coordinatorLayout;

    ProgressDialog progressDialog;

    private SQLiteHandler db;
    private SessionManager session;

    private static final String TAG = "RecipeActivity";

    int recipe_status = 0;

    String addImage, addName, addCategory, addPrepTime, addCookTime, addDesc, addIngredients, addSteps, addComment, addSource, addUrl, addVideoUrl;
    String recipeUniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        toolbarRecipe = findViewById(R.id.toolbar_recipe);
        coordinatorLayout = findViewById(R.id.root_layout);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.recycler_view);
        listRecipe = new ArrayList<>();
        recipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(getApplicationContext(), listRecipe);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);

        setSupportActionBar(toolbarRecipe);
        getSupportActionBar().setTitle("Recipes");
        toolbarRecipe.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipeRecyclerViewAdapter);

        // adding item touch helper only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelper = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

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

        StringRequest arrayRequest = new StringRequest(Request.Method.GET, AppConfig.URL_RETRIEVE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray array = new JSONArray(response);

                    //listRecipe.clear();

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);

                        recipeUniqueId = jsonObject.getString("uid");

                        listRecipe.add(new Recipe(
                                jsonObject.getString("id"),
                                jsonObject.getString("uid"),
                                jsonObject.getString("name"),
                                jsonObject.getString("category"),
                                jsonObject.getString("description"),
                                jsonObject.getString("image"),
                                jsonObject.getString("preptime"),
                                jsonObject.getString("cooktime"),
                                jsonObject.getString("ingredient"),
                                jsonObject.getString("step"),
                                jsonObject.getString("comment"),
                                jsonObject.getString("source"),
                                jsonObject.getString("url"),
                                jsonObject.getString("videourl")

                        ));


                        // refreshing recycler view
                        recipeRecyclerViewAdapter.notifyDataSetChanged();

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
                final List<Recipe> filterModelList = filter(listRecipe, newText);
                recipeRecyclerViewAdapter.setFilter(filterModelList);
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


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof RecipeRecyclerViewAdapter.MyViewHolder) {

            recipe_status = 0;

            // get the removed item name to display it in snack bar
            addImage = listRecipe.get(viewHolder.getAdapterPosition()).getThumbnail();
            addName = listRecipe.get(viewHolder.getAdapterPosition()).getName();
            addCategory = listRecipe.get(viewHolder.getAdapterPosition()).getCategory();
            addPrepTime = listRecipe.get(viewHolder.getAdapterPosition()).getPreptime();
            addCookTime = listRecipe.get(viewHolder.getAdapterPosition()).getCooktime();
            addDesc = listRecipe.get(viewHolder.getAdapterPosition()).getDescription();
            addIngredients = listRecipe.get(viewHolder.getAdapterPosition()).getIngredients();
            addSteps = listRecipe.get(viewHolder.getAdapterPosition()).getSteps();
            addComment = listRecipe.get(viewHolder.getAdapterPosition()).getComments();
            addSource = listRecipe.get(viewHolder.getAdapterPosition()).getSource();
            addUrl = listRecipe.get(viewHolder.getAdapterPosition()).getUrl();
            addVideoUrl = listRecipe.get(viewHolder.getAdapterPosition()).getVideourl();

            // backup of removed item for undo purpose
            final Recipe deletedRecipe = listRecipe.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {
                // remove the item from recycler view
                recipeRecyclerViewAdapter.removeItem(deletedIndex);

                // updating recyclerView
                recyclerView.setAdapter(recipeRecyclerViewAdapter);

                //delete recipe from db
                deleteUpdateRecipeInDatabase(addName, recipe_status);

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar.make(coordinatorLayout, addName + " deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("RESTORE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        recipe_status = 1;

                        // restore the item to the recycler view
                        recipeRecyclerViewAdapter.restoreItem(deletedIndex, deletedRecipe);

                        deleteUpdateRecipeInDatabase(addName, recipe_status);

                        // updating recyclerView
                        recyclerView.setAdapter(recipeRecyclerViewAdapter);

                    }
                });

                snackbar.setDuration(10000);
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();

            } else if (direction == ItemTouchHelper.RIGHT) {
                Intent intentUpdateRecipe = new Intent(getApplicationContext(), UpdateActivity.class);

                //prepare data to be updated
                intentUpdateRecipe.putExtra("uid", recipeUniqueId);
                intentUpdateRecipe.putExtra("image", addImage);
                intentUpdateRecipe.putExtra("name", addName);
                intentUpdateRecipe.putExtra("category", addCategory);
                intentUpdateRecipe.putExtra("preptime", addPrepTime);
                intentUpdateRecipe.putExtra("cooktime", addCookTime);
                intentUpdateRecipe.putExtra("description", addDesc);
                intentUpdateRecipe.putExtra("ingredients", addIngredients);
                intentUpdateRecipe.putExtra("steps", addSteps);
                intentUpdateRecipe.putExtra("comments", addComment);
                intentUpdateRecipe.putExtra("source", addSource);
                intentUpdateRecipe.putExtra("url", addUrl);
                intentUpdateRecipe.putExtra("videourl", addVideoUrl);

                // updating recyclerView
                recyclerView.setAdapter(recipeRecyclerViewAdapter);

                startActivity(intentUpdateRecipe);
            }

        }
    }


    private void deleteUpdateRecipeInDatabase(final String name, final int recipe_status) {

        // Tag used to cancel the request
        //String cancel_req_tag = "delete";

        if (recipe_status == 1) {
            progressDialog.setMessage("Restoring " + name + " ...");
        } else {
            progressDialog.setMessage("Deleting " + name + " ...");
        }

        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_DELETE_RECIPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Delete Response: " + response);
                hideDialog();
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    int delete_restore = jObj.getInt("delete_restore");

                    if (!error) {

                        if (delete_restore == 0) {
                            Toast.makeText(getApplicationContext(), name + " deleted successfully.", Toast.LENGTH_LONG).show();
                        } else if (delete_restore == 1) {
                            Toast.makeText(getApplicationContext(), name + " restored successfully.", Toast.LENGTH_LONG).show();
                        }


                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Deletion Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("recipe_status", String.valueOf(recipe_status));
                params.put("name", name);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void logoutUser() {
        session.setLogin(false, 2);

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

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
