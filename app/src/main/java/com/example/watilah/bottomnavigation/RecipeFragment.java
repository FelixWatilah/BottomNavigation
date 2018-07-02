package com.example.watilah.bottomnavigation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;

public class RecipeFragment extends Fragment {

    List<Recipe> listRecipe = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapterRecipe recyclerViewAdapterRecipe;

    private ShimmerFrameLayout mShimmerViewContainer;

    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jsonCall();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_recipe, container, false);

        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        recyclerView = v.findViewById(R.id.recycler_view);

        recyclerViewAdapterRecipe = new RecyclerViewAdapterRecipe(getContext(), listRecipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapterRecipe);

        // Inflate the layout for this fragment
        return v;
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

                Toast.makeText(getContext(), "Size of List" + String.valueOf(listRecipe.size()), Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, listRecipe.get(1).toString(), Toast.LENGTH_SHORT).show();

                //setRvAdapter(listRecipe);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(arrayRequest);
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
