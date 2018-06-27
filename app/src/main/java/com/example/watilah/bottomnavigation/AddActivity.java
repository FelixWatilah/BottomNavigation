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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    String URL = "http://10.0.2.2/CRUD/add_recipe.php";
    ProgressDialog progressDialog;
    private static final String TAG = "AddActivity";

    private FloatingActionButton floatingActionButton;
    private ImageView image;
    private TextInputEditText recipeName, category, preparationTime, cookTime, description, ingredient, step, comment, source, url, videoUrl;
    private Button save;
    private Bitmap bitmap;
    private String addImage, addName, addCategory, addPrepTime, addCookTime, addDesc, addIngr, addStep, addComment, addSource, addUrl, addVideoUrl;

    final ArrayList<CategoryModel> categoryList = new ArrayList<>();

    // String array for alert dialog multi choice items
    final String[] categories = new String[]{"Starter", "Main course", "Dessert", "Other"};

    // Boolean array for initial selected items
    final boolean[] checkedCategories = new boolean[]{false, false, false, false};

    private int GALLERY = 1, CAMERA = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Toolbar
        Toolbar toolbarAdd = findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbarAdd);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add New Recipe");
        toolbarAdd.setTitleTextColor(Color.parseColor("#ffffff"));

        floatingActionButton = findViewById(R.id.fab_take_image);
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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPictureDialog();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addImage = image.getDrawable().toString();
                addName = recipeName.getText().toString();
                addCategory = category.getText().toString();
                addPrepTime = preparationTime.getText().toString();
                addCookTime = cookTime.getText().toString();
                addDesc = description.getText().toString();
                addIngr = ingredient.getText().toString();
                addStep = step.getText().toString();
                addComment = comment.getText().toString();
                addSource = source.getText().toString();
                addUrl = url.getText().toString();
                addVideoUrl = videoUrl.getText().toString();

                saveData();

            }
        });

    }

    private void saveData() {

        // Tag used to cancel the request
        String cancel_req_tag = "add";

        progressDialog.setMessage("Saving your Recipe...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String recipe = jObj.getJSONObject("recipe").getString("name");
                        Toast.makeText(getApplicationContext(), recipe + ", has been successfully saved!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                /*if (response.equals("1")) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else if (response.equals("2")) {
                    Toast.makeText(getApplicationContext(), "It seems the recipe already exists.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Saving Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("image", imageToString(bitmap));
                params.put("name", addName);
                params.put("category", addCategory);
                params.put("preptime", addPrepTime);
                params.put("cooktime", addCookTime);
                params.put("description", addDesc);
                params.put("ingredient", addIngr);
                params.put("step", addStep);
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
        categoryDialog.setTitle("Categories");

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
                    if (i != selectedList.size() - 1)
                        category.setText(category.getText() + selectedList.get(i).getName() + ", ");
                    else
                        category.setText(category.getText() + selectedList.get(i).getName());
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
                // make sure to clear list that duplication dont formed here
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
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public void takeImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            image.setImageBitmap(thumbnail);
            //saveImage(thumbnail);
            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    /*public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();

            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();

            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }*/

    public String imageToString(Bitmap bitmap){
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

}