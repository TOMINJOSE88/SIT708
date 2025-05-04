package com.example.personalizedlearningexperienceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;


public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone_number";

    private static final String TABLE_INTERESTS = "user_interests";
    private static final String COLUMN_USER = "username";
    private static final String COLUMN_INTEREST = "interest";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_PHONE + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // NEW: interests table
        String CREATE_INTERESTS_TABLE = "CREATE TABLE " + TABLE_INTERESTS + "("
                + COLUMN_USER + " TEXT,"
                + COLUMN_INTEREST + " TEXT"
                + ")";
        db.execSQL(CREATE_INTERESTS_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Insert user
    public boolean addUser(String username, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHONE, phone);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Check login
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Check if user already exists (for Signup validation)
    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean taken = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return taken;
    }

    public void saveUserInterests(String username, Set<String> interests) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INTERESTS, COLUMN_USER + "=?", new String[]{username}); // clear old

        for (String interest : interests) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER, username);
            values.put(COLUMN_INTEREST, interest);
            db.insert(TABLE_INTERESTS, null, values);
        }
        db.close();
    }

    public List<String> getUserInterests(String username) {
        List<String> interests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("user_interests",
                new String[]{"interest"},
                "username=?",
                new String[]{username},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                interests.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return interests;
    }
}
