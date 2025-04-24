package com.example.itubeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "itube.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullname TEXT, " +
                "username TEXT UNIQUE, " +
                "password TEXT)";
        db.execSQL(createUsersTable);

        String createPlaylistTable = "CREATE TABLE playlist (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "url TEXT)";
        db.execSQL(createPlaylistTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS playlist");
        onCreate(db);
    }

    public boolean insertUser(String fullName, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullname", fullName);
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?",
                new String[]{username, password});
        boolean isValid = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isValid;
    }

    public void insertUrl(String url) {
        if (Session.currentUser == null) return;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", Session.currentUser);
        values.put("url", url);
        db.insert("playlist", null, values);
        db.close();
    }

    public List<String> getAllUrls() {
        List<String> urls = new ArrayList<>();
        if (Session.currentUser == null) return urls; // ✅ Avoid crash
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT url FROM playlist WHERE username=?", new String[]{Session.currentUser});
        if (cursor.moveToFirst()) {
            do {
                urls.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return urls;
    }

    public void deleteUrl(String url) {
        if (Session.currentUser == null) return; // ✅ Avoid crash
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("playlist", "username=? AND url=?", new String[]{Session.currentUser, url});
        db.close();
    }

}
