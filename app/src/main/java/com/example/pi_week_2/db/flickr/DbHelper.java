package com.example.pi_week_2.db.flickr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "flick_db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
        db.execSQL("CREATE TABLE tags (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, user_code INTEGER, FOREIGN KEY(user_code) REFERENCES users(_id));");
        db.execSQL("CREATE TABLE photos (_id INTEGER PRIMARY KEY AUTOINCREMENT, link TEXT, tag_code INTEGER, FOREIGN KEY(tag_code) REFERENCES tags(_id));");
        db.execSQL("CREATE TABLE history (_id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT, user_code INTEGER, FOREIGN KEY(user_code) REFERENCES users(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        db.execSQL("PRAGMA foreign_keys=on");
    }
}
