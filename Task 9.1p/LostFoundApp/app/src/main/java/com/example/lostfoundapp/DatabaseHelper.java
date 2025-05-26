package com.example.lostfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LostFound.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "items";

    private static final String COL_ID = "id";
    private static final String COL_TYPE = "type";
    private static final String COL_NAME = "name";
    private static final String COL_PHONE = "phone";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_DATE = "date";
    private static final String COL_LOCATION = "location";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TYPE + " TEXT, " +
                COL_NAME + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_LOCATION + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertItem(String type, String name, String phone, String description, String date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TYPE, type);
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_DATE, date);
        values.put(COL_LOCATION, location);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1; // if insert fails, it returns -1
    }

    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);
    }

    public boolean deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public List<Item> getAllItemsList() {
        List<Item> itemList = new ArrayList<>();
        Cursor cursor = getAllItems(); // already existing method
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String type = cursor.getString(1);
                String name = cursor.getString(2);
                String phone = cursor.getString(3);
                String description = cursor.getString(4);
                String date = cursor.getString(5);
                String location = cursor.getString(6);
                itemList.add(new Item(id, type, name, phone, description, date, location));
            } while (cursor.moveToNext());
        }
        return itemList;
    }


}
