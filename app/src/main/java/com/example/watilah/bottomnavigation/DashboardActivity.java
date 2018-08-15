package com.example.watilah.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {

    TextView tvCountRecipes, tvDeletedRecipes, tvUsers;
    CardView cardViewRecipe, cardAddRecipe, cardAddUser, cardManageUsers, cardDeleted;

    Button logoutAdmin;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cardViewRecipe = findViewById(R.id.card_view_recipe);
        cardAddRecipe = findViewById(R.id.card_add_recipe);
        cardAddUser = findViewById(R.id.card_add_user);
        cardManageUsers = findViewById(R.id.card_manage_users);
        cardDeleted = findViewById(R.id.card_deleted_recipe);
        tvCountRecipes = findViewById(R.id.count_recipes);
        tvDeletedRecipes = findViewById(R.id.deleted_recipes);
        tvUsers = findViewById(R.id.users);
        logoutAdmin = findViewById(R.id.logout_admin);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        cardViewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recipeIntent = new Intent(getApplicationContext(), RecipeActivity.class);
                startActivity(recipeIntent);
            }
        });

        cardAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(addIntent);
            }
        });

        cardAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addUserIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(addUserIntent);
            }
        });

        cardManageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateIntent = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(updateIntent);
            }
        });

        cardDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deletedIntent = new Intent(getApplicationContext(), DeletedActivity.class);
                startActivity(deletedIntent);
            }
        });

        logoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        jsonCall();

    }

    public void jsonCall() {

        StringRequest arrayRequest = new StringRequest(Request.Method.GET, AppConfig.URL_RETRIEVE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                if (response == null) {

                    Toast.makeText(getApplicationContext(), "Couldn't fetch the recipes! Pleas try again.", Toast.LENGTH_LONG).show();

                } else {

                    try {

                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject jsonObject = array.getJSONObject(i);

                            String numberOfRecipes = jsonObject.getString("number_of_recipes");
                            String deletedRecipes = jsonObject.getString("number_of_deleted_recipes");
                            String users = jsonObject.getString("users");
                            tvCountRecipes.setText(numberOfRecipes);
                            tvDeletedRecipes.setText(deletedRecipes);
                            tvUsers.setText(users);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

    private void logoutUser() {
        session.setLogin(false,2);

        db.deleteUsers();

        // Launching the welcome activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
