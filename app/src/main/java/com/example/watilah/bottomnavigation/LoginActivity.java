package com.example.watilah.bottomnavigation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private TextInputEditText mLogEmail, mLogPassword;
    private ProgressDialog pDialog;

    private String email, password;

    //String LOGIN_URL = "http://10.0.2.2/CRUD/login.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogEmail = findViewById(R.id.log_email);
        mLogPassword = findViewById(R.id.log_password);

        Button mLogBtn = findViewById(R.id.log_btn);
        TextView mToRegister = findViewById(R.id.toRegister);
        TextView mMore = findViewById(R.id.more);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // More button Click Event
        mMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent toMainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(toMainIntent);
            }
        });

        // Login button Click Event
        mLogBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                email = mLogEmail.getText().toString().trim();
                password = mLogPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    login(email, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }

            }

        });

        // Link to Register Screen
        mToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent toRegisterIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(toRegisterIntent);
                finish();
            }
        });

    }

    // Login Method
    public void login(final String email, final String password) {

        // Tag used to cancel the request
        String cancel_req_tag = "login";
        pDialog.setMessage("Logging you in...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Register Response: " + response);
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");

                            if (!error) {
                                String user = jObj.getJSONObject("user").getString("name");
                                // Launch User activity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("username", user);
                                startActivity(intent);
                                finish();
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

                        //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
