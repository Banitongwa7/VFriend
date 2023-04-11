package com.example.vfriend.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.vfriend.DatabaseHelper;
import com.example.vfriend.User;

public class UserDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", user.getEmail());
        contentValues.put("password", user.getPassword());
        contentValues.put("fullname", user.getFullname());
        long result = sqLiteDatabase.insert("user", null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String[] projection = {"email"};
        String selection = "email = ?";
        String[] selectionArgs = {email};
        Cursor cursor = sqLiteDatabase.query("user", projection, selection, selectionArgs, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public User checkUser(String email, String password) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM user WHERE email = '" + email + "' AND password = '" + password + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            User user = new User();
            user.setEmail(cursor.getString(0));
            user.setPassword(cursor.getString(1));
            user.setFullname(cursor.getString(2));
            return user;
        }
        return null;
    }

}
