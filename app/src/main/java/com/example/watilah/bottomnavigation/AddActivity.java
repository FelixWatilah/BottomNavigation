package com.example.watilah.bottomnavigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.watilah.bottomnavigation.helper.SQLiteHandler;
import com.example.watilah.bottomnavigation.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AddActivity extends AppCompatActivity {
    private static final String TAG = AddActivity.class.getSimpleName();
    private Button mRegBtn;
    private TextView mToLogin;
    private TextInputEditText mRegName;
    private TextInputEditText mRegEmail;
    private TextInputEditText mRegPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private ImageView addImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Spinner
//        Spinner spinnerCategory = findViewById(R.id.recipe_category);
//        String[] categories = {"Select", "Appetizers & Beverages", "Soups & Salads", "Side Dishes", "Main Dishes", "One Dish Meals", "Desserts", "Other"};
//        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, categories);
//        spinnerCategory.setAdapter(categoriesAdapter);

        // Toolbar
        Toolbar toolbarAdd = findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbarAdd);
        getSupportActionBar().setTitle("Add New Recipe");
        toolbarAdd.setTitleTextColor(Color.parseColor("#ffffff"));

        mRegName = findViewById(R.id.recipe_name);
//        mRegEmail = findViewById(R.id.recipe_category);
        mRegPassword = findViewById(R.id.recipe_cook_time);
        mRegBtn = findViewById(R.id.reg_btn);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(AddActivity.this,
                    WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = mRegName.getText().toString().trim();
                String email = mRegEmail.getText().toString().trim();
                String password = mRegPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });

        addImage = findViewById(R.id.add_image);

        loadImage();

    }

    private void loadImage(){
        GlideApp
                .with(getApplicationContext())
                .load(R.drawable.smart_cake)
                .placeholder(R.drawable.cake)
                .into(addImage);
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String name, final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(AddActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
