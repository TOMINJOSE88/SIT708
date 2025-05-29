package com.example.cookwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "CookWise.db";
    private static final int DB_VERSION = 2; // Incremented version

    // User table
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    // Recipe table
    private static final String TABLE_RECIPES = "recipes";
    private static final String COL_RECIPE_ID = "recipe_id";
    private static final String COL_RECIPE_EMAIL = "user_email";
    private static final String COL_RECIPE_TEXT = "recipe_text";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsers = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT, "
                + COL_EMAIL + " TEXT UNIQUE, "
                + COL_PASSWORD + " TEXT)";
        db.execSQL(createUsers);

        // Create recipes table
        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " ("
                + COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RECIPE_EMAIL + " TEXT, "
                + COL_RECIPE_TEXT + " TEXT)";
        db.execSQL(createRecipes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // Register a new user
    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Login check
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Save a recipe for a user
    public boolean saveRecipe(String email, String recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_EMAIL, email);
        values.put(COL_RECIPE_TEXT, recipe);
        long result = db.insert(TABLE_RECIPES, null, values);
        return result != -1;
    }

    public ArrayList<String> getRecipeTitles(String email) {
        ArrayList<String> titles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT recipe_text FROM recipes WHERE user_email=?", new String[]{email});
        while (cursor.moveToNext()) {
            String recipe = cursor.getString(0);
            String title = recipe.split("\n")[0].replace("Recipe Name:", "").trim();
            titles.add(title);
        }
        cursor.close();
        return titles;
    }

    public boolean deleteRecipeByTitle(String email, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("recipes", "user_email=? AND recipe_text LIKE ?", new String[]{email, "%" + title + "%"}) > 0;
    }


    public String getUserNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_NAME + " FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        return "User";
    }

    public String getAllRecipesAsString(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_RECIPE_TEXT + " FROM " + TABLE_RECIPES + " WHERE " + COL_RECIPE_EMAIL + "=?",
                new String[]{userEmail}
        );

        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            builder.append(cursor.getString(0)).append("\n\n");
        }
        cursor.close();
        return builder.toString().trim();
    }


    public boolean updateUserProfile(String oldEmail, String newName, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, newName);
        values.put(COL_EMAIL, newEmail);

        // Update row where email matches oldEmail
        int result = db.update(TABLE_USERS, values, COL_EMAIL + " = ?", new String[]{oldEmail});
        return result > 0;
    }



    public ArrayList<String> getSavedRecipes(String email) {
        ArrayList<String> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_RECIPE_TEXT + " FROM " + TABLE_RECIPES +
                " WHERE " + COL_RECIPE_EMAIL + "=?", new String[]{email});
        while (cursor.moveToNext()) {
            recipes.add(cursor.getString(0));
        }
        cursor.close();
        return recipes;
    }
}
