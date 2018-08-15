package com.example.watilah.bottomnavigation;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class UpdateActivity extends AppCompatActivity {

    private static final String TAG = "AddActivity";

    ProgressDialog progressDialog;
    String ingredientChildTextViewValue, stepChildTextViewValue, addIngredients, addSteps, addName, addCategory, addPrepTime, addCookTime, addDesc, addComment, addSource, addUrl, addVideoUrl;
    String addUniqueId;
    private FloatingActionButton fabTakeImage;
    private ImageView image;
    private TextInputEditText recipeName, category, preparationTime, cookTime, description, ingredient, step, comment, source, url, videoUrl;
    private Button save;
    private Bitmap bitmap;
    ArrayList<String> allStepsArray = new ArrayList<>();
    ArrayList<String> allIngredientsArray = new ArrayList<>();
    final ArrayList<CategoryModel> categoryList = new ArrayList<>();

    StringBuilder allTextStep = new StringBuilder();
    StringBuilder allTextIngredient = new StringBuilder();

    // String array for alert dialog multi choice items
    final String[] categories = new String[]{
            "Appetizers & Snacks",
            "Breads & Pastries",
            "Drinks & Cocktails",
            "Non-Vegetarian",
            "Vegetarian",
            "Soups",
            "Salads & Dressings",
            "Sandwiches & Wraps",
            "Sweets & Desserts",
            "Starter",
            "Main course",
            "Other"
    };

    // Boolean array for initial selected items
    final boolean[] checkedCategories = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false};

    private final int GALLERY = 1, CAMERA = 2;

    private ImageButton buttonAddIngredient, buttonAddStep, mic;
    private LinearLayout container, containerStep;

    private final int REQ_CODE_VOICE_INPUT = 100;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        buttonAddIngredient = findViewById(R.id.btn_add_ingredient);
        container = findViewById(R.id.container);
        buttonAddStep = findViewById(R.id.btn_add_step);
        containerStep = findViewById(R.id.container_step);

        mic = findViewById(R.id.mic);

        fabTakeImage = findViewById(R.id.fab_take_image);
        image = findViewById(R.id.image_from_camera);

        recipeName = findViewById(R.id.add_recipe_name);
        category = findViewById(R.id.add_category);
        preparationTime = findViewById(R.id.add_preparation_time);
        cookTime = findViewById(R.id.add_cook_time);
        description = findViewById(R.id.add_description);
        ingredient = findViewById(R.id.add_ingredient);
        step = findViewById(R.id.add_step);
        comment = findViewById(R.id.add_comment);
        source = findViewById(R.id.add_source);
        url = findViewById(R.id.add_url);
        videoUrl = findViewById(R.id.add_video_url);

        save = findViewById(R.id.add_save);

        category.setInputType(InputType.TYPE_NULL);

        Intent intent = getIntent();

        addUniqueId = intent.getExtras().getString("uid");
        String addImage = intent.getExtras().getString("image");
        addName = intent.getExtras().getString("name");
        addCategory = intent.getExtras().getString("category");
        addPrepTime = intent.getExtras().getString("preptime");
        addCookTime = intent.getExtras().getString("cooktime");
        addDesc = intent.getExtras().getString("description");
        addIngredients = intent.getExtras().getString("ingredients");
        addSteps = intent.getExtras().getString("steps");
        addComment = intent.getExtras().getString("comments");
        addSource = intent.getExtras().getString("source");
        addUrl = intent.getExtras().getString("url");
        addVideoUrl = intent.getExtras().getString("videourl");

        recipeName.setText(addName);
        category.setText(addCategory);
        preparationTime.setText(addPrepTime);
        cookTime.setText(addCookTime);
        description.setText(addDesc);
        ingredient.setText(addIngredients);
        step.setText(addSteps);
        comment.setText(addComment);
        source.setText(addSource);
        url.setText(addUrl);
        videoUrl.setText(addVideoUrl);


        GlideApp.with(this)
                .load(addImage)
                .centerCrop()
                .into(image);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // Toolbar
        Toolbar toolbarAdd = findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbarAdd);
        getSupportActionBar().setTitle(addName);
        toolbarAdd.setTitleTextColor(Color.parseColor("#ffffff"));

        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setText("");
                showCategoryDialog();
            }
        });

        category.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    category.setText("");
                    showCategoryDialog();
                }
            }
        });

        fabTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPictureDialog();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addIngredients = null;
                addSteps = null;

                addName = recipeName.getText().toString().trim();
                addCategory = category.getText().toString().trim();
                addPrepTime = preparationTime.getText().toString().trim();
                addCookTime = cookTime.getText().toString().trim();
                addDesc = description.getText().toString().trim();
                addComment = comment.getText().toString().trim();
                addSource = source.getText().toString().trim();
                addUrl = url.getText().toString().trim();
                addVideoUrl = videoUrl.getText().toString().trim();

                if (ingredient.getText().toString().equals("")) {
                    addIngredients = allTextIngredient.toString().trim();
                } else {
                    addIngredients = allTextIngredient.toString() + ingredient.getText().toString().trim();
                }

                if (step.getText().toString().equals("")) {
                    addSteps = allTextStep.toString().trim();
                } else {
                    addSteps = allTextStep.toString() + step.getText().toString().trim();
                }

                int position = allIngredientsArray.size();

                Log.v("Ingredient ", addIngredients);
                Log.v("Steps ", addSteps);
                Log.v("Size of Array", "" + position);

                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), allTextIngredient, Toast.LENGTH_SHORT).show();

                saveData();


            }
        });

        buttonAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View addView = layoutInflater.inflate(R.layout.new_ingredient, null);
                final TextInputEditText textOut = addView.findViewById(R.id.textout);
                textOut.setText(ingredient.getText().toString());

                ImageButton buttonRemove = addView.findViewById(R.id.remove);

                final View.OnClickListener thisListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //remove view
                        ((LinearLayout) addView.getParent()).removeView(addView);

                        listAllAddViewIngredient();
                    }
                };

                buttonRemove.setOnClickListener(thisListener);
                container.addView(addView);

                listAllAddViewIngredient();

            }
        });

        buttonAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View addStepView = layoutInflater.inflate(R.layout.new_step, null);
                TextInputEditText textOut = addStepView.findViewById(R.id.textout_step);
                textOut.setText(step.getText().toString());

                ImageButton buttonRemove = addStepView.findViewById(R.id.remove_step);

                final View.OnClickListener thisListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) addStepView.getParent()).removeView(addStepView);
                        //allStepsArray.remove(step.getText().toString());
                        listAllAddViewStep();
                    }
                };

                buttonRemove.setOnClickListener(thisListener);
                containerStep.addView(addStepView);

                listAllAddViewStep();

            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVoiceInput();
            }
        });

    }

    private void getVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please read your description.");

        try {
            startActivityForResult(intent, REQ_CODE_VOICE_INPUT);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {

        // Tag used to cancel the request
        String cancel_req_tag = "add";

        progressDialog.setMessage("Updating your Recipe...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPDATE_RECIPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Update Recipe Response: " + response);
                hideDialog();

                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String recipe = jObj.getJSONObject("recipe").getString("name");
                        Toast.makeText(getApplicationContext(), recipe + ", has been successfully updated!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                        startActivity(intent);
                        finish();
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

                Log.e(TAG, "Submission Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("uid",addUniqueId);
                params.put("image", imageToString(bitmap));
                params.put("name", addName);
                params.put("category", addCategory);
                params.put("preptime", addPrepTime);
                params.put("cooktime", addCookTime);
                params.put("description", addDesc);
                params.put("ingredient", addIngredients);
                params.put("step", addSteps);
                params.put("comment", addComment);
                params.put("source", addSource);
                params.put("url", addUrl);
                params.put("videourl", addVideoUrl);

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

    private void showCategoryDialog() {
        AlertDialog.Builder categoryDialog = new AlertDialog.Builder(this);

        categoryDialog.setCancelable(false);
        categoryDialog.setTitle("Category");

        // make a list to hold state of every color
        for (int i = 0; i < categories.length; i++) {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setName(categories[i]);
            categoryModel.setSelected(checkedCategories[i]);
            categoryList.add(categoryModel);
        }

        // Do something here to pass only ArrayList on this both arrays ('categories' & 'checkedCategories')
        categoryDialog.setMultiChoiceItems(categories, checkedCategories, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // set state to vo in list
                categoryList.get(which).setSelected(isChecked);
                //Toast.makeText(getApplicationContext(), categoryList.get(which).getName() + " " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        categoryDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //selectedCategory.setText("Your preferred categories..... \n");

                // save state of selected vos
                ArrayList<CategoryModel> selectedList = new ArrayList<>();

                for (int i = 0; i < categoryList.size(); i++) {

                    CategoryModel categoryModel = categoryList.get(i);
                    categories[i] = categoryModel.getName();
                    checkedCategories[i] = categoryModel.isSelected();

                    if (categoryModel.isSelected()) {
                        selectedList.add(categoryModel);
                    }
                }

                for (int i = 0; i < selectedList.size(); i++) {
                    // if element is last then not attach comma or attach it
                    if (i != selectedList.size() - 1) {
                        category.setText(String.format("%s%s, ", category.getText(), selectedList.get(i).getName()));
                    } else
                        category.setText(String.format("%s%s", category.getText(), selectedList.get(i).getName()));
                }
                categoryList.clear();
            }
        });

        categoryDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // make sure to clear list that duplication dont formed here
                categoryList.clear();
            }
        });

        categoryDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // make sure to clear list so that duplication don't get formed here
                categoryList.clear();
            }
        });

        categoryDialog.show();
    }

    private void showPictureDialog() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);

        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera"};
        pictureDialog.setTitle("Select Action");
        pictureDialog.setCancelable(true);

        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takeImageFromGallery();
                        break;
                    case 1:
                        takeImageFromCamera();
                        break;
                }
            }
        });
        pictureDialog.show();
    }

    public void takeImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public void takeImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA && resultCode == RESULT_OK) {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            image.setImageBitmap(bitmap);
        }

        switch (requestCode) {
            case REQ_CODE_VOICE_INPUT: {
                if (requestCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    description.setText(result.get(0));
                }
            }
            break;
        }

    }

    public String imageToString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);

    }

    private class CategoryModel {
        private String name;
        private boolean selected;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    private void listAllAddViewIngredient() {

        int childCount = container.getChildCount();

        for (int i = 0; i < childCount; i++) {

            View thisChild = container.getChildAt(i);
            TextInputEditText childTextView = thisChild.findViewById(R.id.textout);

            ingredientChildTextViewValue = childTextView.getText().toString().trim() + ",\n";

            allTextIngredient.append(ingredientChildTextViewValue);
            allIngredientsArray.add(ingredientChildTextViewValue);
        }

        ingredient.setText("");

    }

    private void listAllAddViewStep() {

        int stepChildCount = containerStep.getChildCount();

        for (int i = 0; i < stepChildCount; i++) {

            View thisStepChild = containerStep.getChildAt(i);
            TextInputEditText stepChildTextView = thisStepChild.findViewById(R.id.textout_step);
            stepChildTextViewValue = stepChildTextView.getText().toString().trim() + ",\n";
            allTextStep.append(stepChildTextViewValue);
            allStepsArray.add(stepChildTextViewValue);
        }

        step.setText("");

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

}