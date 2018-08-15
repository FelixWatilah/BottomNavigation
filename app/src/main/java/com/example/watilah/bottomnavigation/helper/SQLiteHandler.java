package com.example.watilah.bottomnavigation.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "recipe";

    // Database Path
    //private static final String DATABASE_PATH = "/data/data/com.example.watilah.bottomnavigation/databases/";

    // Tables
    private static final String TABLE_USER = "user";
    private static final String TABLE_RECIPE = "recipe_tb";
    private static final String TABLE_FAVORITES = "favorites";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "recipe_name";
    private static final String RECIPE_CATEGORY = "category";
    private static final String RECIPE_PREPTIME = "preptime";
    private static final String RECIPE_COOKTIME = "cooktime";
    private static final String RECIPE_DESCRIPTION = "description";
    private static final String RECIPE_INGREDIENTS = "ingredient";
    private static final String RECIPE_STEPS = "step";
    private static final String RECIPE_COMMENT = "comment";
    private static final String RECIPE_SOURCE = "source";
    private static final String RECIPE_URL = "url";
    private static final String RECIPE_VIDEO_URL = "videourl";
    private static final String RECIPE_STATUS = "recipe_status";
    private static final String RECIPE_FAVORITE = "favorite";

    private static final String FAVORITE_ID = "id";
    private static final String FAVORITE_FOOD_ID = "foodId";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_RECIPE + " ("
                + RECIPE_ID + " INTEGER PRIMARY KEY," + RECIPE_NAME + " TEXT,"
                + RECIPE_CATEGORY + " TEXT," + RECIPE_PREPTIME + " TEXT,"
                + RECIPE_COOKTIME + " TEXT," + RECIPE_DESCRIPTION + " TEXT,"
                + RECIPE_INGREDIENTS + " TEXT," + RECIPE_STEPS + " TEXT,"
                + RECIPE_COMMENT + " TEXT," + RECIPE_SOURCE + " TEXT,"
                + RECIPE_URL + " TEXT," + RECIPE_VIDEO_URL + " TEXT,"
                + RECIPE_STATUS + " TEXT," + RECIPE_FAVORITE + " TEXT" + " )";

        db.execSQL(CREATE_RECIPE_TABLE);

        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + " ( "
                + FAVORITE_ID + " INTEGER PRIMARY KEY, " + FAVORITE_FOOD_ID + " TEXT UNIQUE " + " )";

        db.execSQL(CREATE_FAVORITES_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);

        // Create tables again
        onCreate(db);
    }


   /* private boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(SQLiteException e)
        {
        }
        return checkDB;
    }
*/

    /**
     * Storing user details in database
     */
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Storing recipe in database
     */
    public void addRecipe(String recipe_name, String category, String preptime, String cooktime,
                          String description, String ingredient, String step, String comment,
                          String source, String url, String videourl) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, recipe_name);
        values.put(RECIPE_CATEGORY, category);
        values.put(RECIPE_PREPTIME, preptime);
        values.put(RECIPE_COOKTIME, cooktime);
        values.put(RECIPE_DESCRIPTION, description);
        values.put(RECIPE_INGREDIENTS, ingredient);
        values.put(RECIPE_STEPS, step);
        values.put(RECIPE_COMMENT, comment);
        values.put(RECIPE_SOURCE, source);
        values.put(RECIPE_URL, url);
        values.put(RECIPE_VIDEO_URL, videourl);

        // Inserting Row
        long id = db.insert(TABLE_RECIPE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);

    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getRecipes() {
        HashMap<String, String> recipe = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECIPE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            recipe.put("recipe_name", cursor.getString(1));
            recipe.put("category", cursor.getString(2));
            recipe.put("preptime", cursor.getString(3));
            recipe.put("cooktime", cursor.getString(4));
            recipe.put("description", cursor.getString(5));
            recipe.put("ingredient", cursor.getString(6));
            recipe.put("step", cursor.getString(7));
            recipe.put("comment", cursor.getString(8));
            recipe.put("source", cursor.getString(9));
            recipe.put("url", cursor.getString(10));
            recipe.put("videourl", cursor.getString(11));
            recipe.put("recipe_status", cursor.getString(12));
            recipe.put("favorite", cursor.getString(13));
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching recipes from Sqlite: " + recipe.toString());

        return recipe;
    }

    /**
     * Recreate database, Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void addToFavorites(String foodId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO " + TABLE_FAVORITES + " (" + FAVORITE_FOOD_ID+ ") VALUES(%S); ", foodId);
        db.execSQL(query);
    }

    public void removeFromFavorites(String foodId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM " + TABLE_FAVORITES + " WHERE " + FAVORITE_FOOD_ID + "='%s';", foodId);
        db.execSQL(query);
    }

    public boolean isFavorites(String foodId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM " + TABLE_FAVORITES + " WHERE " + FAVORITE_FOOD_ID + "='%s'; ", foodId);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}
