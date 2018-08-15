package com.example.watilah.bottomnavigation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.watilah.bottomnavigation.helper.SQLiteHandler;
import com.example.watilah.bottomnavigation.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private TextInputEditText mLogEmail, mLogPassword;
    private ProgressDialog pDialog;

    private String email, password;

    private SessionManager session;
    private SQLiteHandler db;

    //String LOGIN_URL = "http://10.0.2.2/CRUD/login.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogEmail = findViewById(R.id.log_email);
        mLogPassword = findViewById(R.id.log_password);

        Button mLogBtn = findViewById(R.id.log_btn);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn() && session.isUserType() == 1) {
            // User is already logged in. Take him to main activity
            Intent intentDash = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intentDash);
            finish();
        } else if (session.isLoggedIn() && session.isUserType() == 0) {
            // User is already logged in. Take him to main activity
            Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();
        }

        // Login button Click Event
        mLogBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                email = mLogEmail.getText().toString().trim();
                password = mLogPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    login(email, password);
                } else {
                    if (email.isEmpty()) {
                        mLogEmail.setError("Email required.");
                    }
                    if (password.isEmpty()) {
                        mLogPassword.setError("Password required.");
                    }

                }

            }

        });

    }

    // Login Method
    public void login(final String email, final String password) {

        // Tag used to cancel the request
        String cancel_req_tag = "login";
        pDialog.setMessage("Logging in " + email + "...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Login Response: " + response);
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");

                            if (!error) {


                                // Now store the user in SQLite
                                String uid = jObj.getString("uid");
                                int userType = jObj.getInt("usertype");

                                // user successfully logged in
                                // Create login session
                                session.setLogin(true, userType);

                                JSONObject user = jObj.getJSONObject("user");

                                String name = user.getString("name");
                                String email = user.getString("email");
                                String created_at = user.getString("created_at");

                                // Inserting row in users table
                                db.addUser(name, email, uid, created_at);

                                // Check if user is already logged in or not
                                if (session.isUserType() == 1) {
                                    // User is already logged in. Take him to main activity
                                    Intent intentDash = new Intent(LoginActivity.this, DashboardActivity.class);
                                    startActivity(intentDash);
                                    finish();
                                } else if (session.isUserType() == 0) {
                                    // User is already logged in. Take him to main activity
                                    Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intentMain);
                                    finish();
                                }


                            } else {

                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        hideDialog();

                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

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
