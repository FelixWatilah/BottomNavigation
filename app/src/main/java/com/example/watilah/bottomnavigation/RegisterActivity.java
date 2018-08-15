package com.example.watilah.bottomnavigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText mRegName, mRegEmail, mRegPassword;
    TextView mToLogin;
    private String name, email, password;
    ProgressDialog progressDialog;
    private static final String TAG = "RegisterActivity";

    private SessionManager session;
    private SQLiteHandler db;
    private RadioGroup userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        mRegName = findViewById(R.id.reg_name);
        mRegEmail = findViewById(R.id.reg_email);
        mRegPassword = findViewById(R.id.reg_password);
        Button mRegisterButton = findViewById(R.id.register);
        mToLogin = findViewById(R.id.login);
        userType = findViewById(R.id.rg_user);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = mRegName.getText().toString();
                email = mRegEmail.getText().toString();
                password = mRegPassword.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !user().isEmpty()) {
                    register(name, email, password, user());
                } else {
                    if (name.isEmpty()) {
                        mRegName.setError("Please enter user's name");
                    }
                    if (email.isEmpty()) {
                        mRegEmail.setError("Please enter user's email");
                    }
                    if (password.isEmpty()) {
                        mRegPassword.setError("Password is required.");
                    }
                    if (user().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please select user.", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        mToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(toLoginIntent);
            }
        });

    }

    public String user() {
        String user;

        int selectedId = userType.getCheckedRadioButtonId();

        RadioButton userRadioButton = findViewById(selectedId);

        if (selectedId == -1) {
            user = "";
        } else {
            user = userRadioButton.getText().toString();
        }

        return user;

    }

    public void register(final String name, final String email, final String password, final String user) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Registering " + name + "...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Register Response: " + response);
                hideDialog();
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        //Log.v("Response :", response);

                        // User successfully stored in MySQL now store the user in sqlite
                        //String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String usertype = user.getString("usertype");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "Successfully registered " + email, Toast.LENGTH_LONG).show();

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

                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("usertype", user);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void logoutUser() {
        session.setLogin(false,2);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

}