package com.example.watilah.bottomnavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView cat, desc, ingredients, steps, preptime, cooktime, comment, source, url, videourl, descTitle;

    private SQLiteHandler db;
    private SessionManager session;
    private FloatingActionButton fabFav, fabSpeech;
    boolean flag = false;
    boolean flagSpeech = true;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        cat = findViewById(R.id.details_cat);
        desc = findViewById(R.id.details_desc);
        ingredients = findViewById(R.id.ingredients);
        steps = findViewById(R.id.steps);
        preptime = findViewById(R.id.preptime);
        cooktime = findViewById(R.id.cooktime);
        comment = findViewById(R.id.comment);
        source = findViewById(R.id.source);
        url = findViewById(R.id.url);
        videourl = findViewById(R.id.videourl);
        fabFav = findViewById(R.id.fab_fav);
        fabSpeech = findViewById(R.id.fab_speech);
        descTitle = findViewById(R.id.desc_title);

        textToSpeech = new TextToSpeech(this, this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Remove shadow from the action bar
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setElevation(0);

        ImageView imageView = findViewById(R.id.img);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing);

        // Receive Data
        Intent intent = getIntent();

        final String Id = Objects.requireNonNull(intent.getExtras()).getString("id");
        final String Uid = Objects.requireNonNull(intent.getExtras()).getString("uid");
        final String Title = Objects.requireNonNull(intent.getExtras()).getString("Title");
        String Image = intent.getExtras().getString("Image");
        String Cat = intent.getExtras().getString("Category");
        String Desc = intent.getExtras().getString("Description");
        String Preptime = intent.getExtras().getString("Preptime");
        String Cooktime = intent.getExtras().getString("Cooktime");
        String Ingredient = intent.getExtras().getString("Ingredient");
        String Step = intent.getExtras().getString("Step");
        String Source = intent.getExtras().getString("Source");
        String Comment = intent.getExtras().getString("Comment");
        String Url = intent.getExtras().getString("Url");
        String Videourl = intent.getExtras().getString("Videourl");

        // Setting data
        collapsingToolbarLayout.setTitle(Title);

        GlideApp.with(this)
                .load(Image)
                .into(imageView);

        cat.setText(Cat);
        desc.setText(Desc);
        ingredients.setText(Ingredient);
        steps.setText(Step);
        preptime.setText("Prep: " + Preptime + " min");
        cooktime.setText("Cook: " + Cooktime + " min");
        source.setText(Source);
        comment.setText(Comment);
        url.setText(Url);
        videourl.setText(Videourl);

        SharedPreferences sharedPreferences = getSharedPreferences("Favorite", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (db.isFavorites(Id)) {

            editor.putBoolean("Favorite Added", true);
            editor.commit();

            fabFav.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite));

            Toast.makeText(getApplicationContext(), "Added to favorite", Toast.LENGTH_SHORT).show();

        }

        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!db.isFavorites(Id)) {

                    db.addToFavorites(Id);

                    editor.putBoolean("Favorite Added", true);
                    editor.commit();

                    fabFav.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite));

                    Toast.makeText(getApplicationContext(), "" + Title + " added to favorite.", Toast.LENGTH_SHORT).show();

                } else {

                    db.removeFromFavorites(Id);

                    editor.putBoolean("Favorite Added", false);
                    editor.commit();

                    fabFav.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border));

                    Toast.makeText(getApplicationContext(), "" + Title + " removed from favorite.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fabSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagSpeech) {
                    fabSpeech.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_volume));
                    voiceOutput();
                    flagSpeech = false;
                } else {
                    fabSpeech.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_volume_off));
                    onDestroy();
                    flagSpeech = true;
                }
            }
        });


    }

    private void voiceOutput() {
        CharSequence descText = desc.getText();
        CharSequence desctitle = descTitle.getText();

        textToSpeech.speak(descText, TextToSpeech.QUEUE_FLUSH, null, "id1");
    }

    private void favoriteRecipe(final String uid, final int fav) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_FAVORITE_RECIPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String recipe = jObj.getJSONObject("recipe").getString("name");
                        String favorite = jObj.getJSONObject("recipe").getString("favorite");

                        Toast.makeText(getApplicationContext(), recipe + ", is your favorite", Toast.LENGTH_SHORT).show();

                        if (Integer.parseInt(favorite) == 1) {
                            flag = true;
                        } else {
                            flag = false;
                        }


                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), "Error Message: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("fav", String.valueOf(fav));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            float i = 50;

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Language not supported", Toast.LENGTH_SHORT).show();
            } else {
                fabSpeech.setEnabled(true);
                voiceOutput();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {

        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();
    }
}
