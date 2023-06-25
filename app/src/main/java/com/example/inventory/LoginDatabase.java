package com.example.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class LoginDatabase extends SQLiteOpenHelper {

    // Model

    private static final String DATABASE_NAME = "login.db";
    private static final int VERSION = 1;

    private final HashMap<String, String> logins;

    private static LoginDatabase loginDatabase;

    public static LoginDatabase getInstance(Context context) {
        if (loginDatabase == null) {
            loginDatabase = new LoginDatabase(context);
        }
        return loginDatabase;
    }

    public LoginDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        logins = new HashMap<>();
    }

    private static final class LoginTable {
        private static final String TABLE = "Login";
        private static final String COL_USERNAME = "Username";
        private static final String COL_PASSWORD = "Password";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Database creation: create table Login (Username text PRIMARY KEY, Password text NOT NULL)
        sqLiteDatabase.execSQL("create table " + LoginTable.TABLE + " (" +
                LoginTable.COL_USERNAME + " text PRIMARY KEY, " +
                LoginTable.COL_PASSWORD + " text NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Recreate the database
        // drop table if exists Login
        sqLiteDatabase.execSQL("drop table if exists " + LoginTable.TABLE);
        onCreate(sqLiteDatabase);
    }

    /**
     * get the HashMap of the stored logins
     * @return the HashMap of username:password combos
     * TODO: research better authentication methods than hashmaps
     */
    protected HashMap<String, String> getLogins() {
        SQLiteDatabase db = this.getReadableDatabase();

        // select * from Login
        String sql = "select * from " + LoginTable.TABLE;
        Cursor cursor = db.rawQuery(sql, null);
        // Check if the cursor exists by moving to first
        if (cursor.moveToFirst()) {
            do {
                // Put the cursor values into the logins HashMap
                logins.put(cursor.getString(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return logins;
    }

    /**
     *
     * @param username the username of the login to add to the database
     * @param password the password of the login to add to the database
     * @return the id of the inserted row or -1 if insert failed
     */
    public boolean addLogin(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        // TODO: possibly check username for valid Email address (@gmail.com, @yahoo.com, etc.)
        values.put(LoginTable.COL_USERNAME, username);
        // TODO: possibly check password strength
        values.put(LoginTable.COL_PASSWORD, password);
        long id = db.insert(LoginTable.TABLE, null, values);
        db.close();
        return id != -1;
    }

    /**
     * Update a login from the database. *Not currently utilized*
     * @param username the username of the login to update
     * @param oldPassword the old password for the login
     * @param newPassword the new password for the login
     */
    /*public void updateLogin(String username, String oldPassword, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        // Don't update the username
        values.put(LoginTable.COL_USERNAME, username);
        // Use the new password
        values.put(LoginTable.COL_PASSWORD, newPassword);
        db.update(LoginTable.TABLE, values,
                // Check the username/password combo: where Username = ? and Password = ?
                LoginTable.COL_USERNAME + " = ? and " + LoginTable.COL_PASSWORD + " = ?",
                new String[]{username, oldPassword});
        db.close();
    }

    *//**
     * Delete a login from the database. *Not currently utilized*
     * @param username the username of the login to be deleted
     * @param password the password of the login to be deleted
     *//*
    public void deleteLogin(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        // Use the login details to verify and delete the row
        db.delete(LoginTable.TABLE,
                LoginTable.COL_USERNAME + " = ? and " + LoginTable.COL_PASSWORD + " = ?",
                new String[]{username, password});
        db.close();
    }*/
}
