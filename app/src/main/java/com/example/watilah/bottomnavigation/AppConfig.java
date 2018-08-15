package com.example.watilah.bottomnavigation;

/**
 * Created by Watilah on 06-Mar-redblue.
 */

public class AppConfig {

    // Server URL
    public static String PATH = "http://18dd477e.ngrok.io";

    // Server user login url
    public static String URL_USERS = PATH + "/recipe/index.php";

    // Server user login url
    public static String URL_LOGIN = PATH + "/recipe/login.php";

    // Server user register url
    public static String URL_REGISTER = PATH + "/recipe/register.php";

    // Server user update recipe url
    public static String URL_UPDATE_RECIPE = PATH + "/recipe/update_recipe.php";

    // Server user update recipe url
    public static String URL_DELETE_RECIPE = PATH + "/recipe/delete_recipe.php";

    // Server user updated recipe url
    public static String URL_DELETED_RECIPE = PATH + "/recipe/deleted_recipe.php";

    // Server recipe url
    public static String URL_ADD_RECIPE = PATH + "/recipe/add_recipe.php";

    // Retrieve recipe url
    public static String URL_RETRIEVE = PATH + "/recipe/retrieve.php";

    public static String URL_FAVORITE_RECIPE  = PATH + "/recipe/favorite.php";
}
