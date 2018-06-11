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

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.watilah.bottomnavigation.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity {
    private static final String TAG = AddActivity.class.getSimpleName();
    private Button mLogBtn, mToRegister, mMore;
    private TextInputEditText mLogEmail, mLogPassword;
    private ProgressDialog pDialog;
    //private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogEmail = findViewById(R.id.log_email);
        mLogPassword = findViewById(R.id.log_password);
        mLogBtn = findViewById(R.id.log_btn);
        mToRegister = findViewById(R.id.toRegister);
        mMore = findViewById(R.id.more);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        //session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

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
                String email = mLogEmail.getText().toString().trim();
                String password = mLogPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }

        });

        // Link to Register Screen
        mToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {

//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "Login Response: " + response.toString());
//                hideDialog();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//
//                    // Check for error node in json
//                    if (!error) {
//                        // user successfully logged in
//                        // Create login session
//                        //session.setLogin(true);
//
//                        // Now store the user in SQLite
//                        String uid = jObj.getString("uid");
//
//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        String email = user.getString("email");
//                        String created_at = user.getString("created_at");
//
//                        // Inserting row in users table
//                        db.addUser(name, email, uid, created_at);
//
//                        // Launch main activity
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        // Error in login. Get the error message
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }





            @Override
            public void onResponse(String Response) {
                try {
                    JSONObject jsonObject=new JSONObject(Response);
                    if(jsonObject.getString("uid").isEmpty()){
                        Toast.makeText(LoginActivity.this,"Failed to log in",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Welcome to the system :"+jsonObject.getString("uid"),Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("email",jsonObject.getString("uid"));
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }








        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
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
