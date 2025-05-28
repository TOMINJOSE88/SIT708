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
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_INTERESTS = "user_interests";
    private static final String TABLE_HISTORY = "quiz_history";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone_number";
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

        String CREATE_INTERESTS_TABLE = "CREATE TABLE " + TABLE_INTERESTS + "("
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_INTEREST + " TEXT"
                + ")";
        db.execSQL(CREATE_INTERESTS_TABLE);

        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT,"
                + "question TEXT,"
                + "your_answer TEXT,"
                + "correct_answer TEXT"
                + ")";
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    // Add user
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
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    public String getEmailByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"email"}, "username=?",
                new String[]{username}, null, null, null);
        String email = null;
        if (cursor.moveToFirst()) {
            email = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return email;
    }


    // Check if username already exists
    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean taken = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return taken;
    }

    // Save selected interests
    public void saveUserInterests(String username, Set<String> interests) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INTERESTS, COLUMN_USERNAME + "=?", new String[]{username}); // clear old

        for (String interest : interests) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_INTEREST, interest);
            db.insert(TABLE_INTERESTS, null, values);
        }
        db.close();
    }

    // Get interests
    public List<String> getUserInterests(String username) {
        List<String> interests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INTERESTS,
                new String[]{COLUMN_INTEREST},
                COLUMN_USERNAME + "=?",
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

    public int[] getQuizStats(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("quiz_history",
                new String[]{"your_answer", "correct_answer"},
                "username=?",
                new String[]{username},
                null, null, null);

        int total = 0;
        int correct = 0;

        if (cursor.moveToFirst()) {
            do {
                total++;
                String yourAnswer = cursor.getString(0);
                String correctAnswer = cursor.getString(1);

                if (yourAnswer != null && yourAnswer.equalsIgnoreCase(correctAnswer)) {
                    correct++;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        int incorrect = total - correct;
        return new int[]{total, correct, incorrect};
    }




    // ✅ Save quiz history
    public void addQuizHistory(String username, String question, String yourAnswer, String correctAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("question", question);
        values.put("your_answer", yourAnswer);
        values.put("correct_answer", correctAnswer);
        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    // ✅ Get quiz history
    public List<String[]> getQuizHistory(String username) {
        List<String[]> history = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT question, your_answer, correct_answer FROM quiz_history WHERE username = ?",
                new String[]{username}
        );

        if (cursor.moveToFirst()) {
            do {
                String[] item = new String[3];
                item[0] = cursor.getString(0); // question
                item[1] = cursor.getString(1); // your answer
                item[2] = cursor.getString(2); // correct answer
                history.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return history;
    }
}
