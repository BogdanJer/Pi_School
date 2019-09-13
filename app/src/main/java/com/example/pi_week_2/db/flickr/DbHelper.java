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
        db.execSQL("CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE)");

        db.execSQL("CREATE TABLE user_favorites(user TEXT, photo TEXT, FOREIGN KEY(user) REFERENCES users(_id), FOREIGN KEY(photo) " +
                "REFERENCES photos(_id));");

        db.execSQL("CREATE TABLE photos (_id INTEGER PRIMARY KEY AUTOINCREMENT, link TEXT, tag TEXT);");
        db.execSQL("CREATE TABLE history (_id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT, user_id INTEGER, FOREIGN KEY(user_id) REFERENCES users(_id))");
        db.execSQL("CREATE TABLE s_photos(_id INTEGER PRIMARY KEY AUTOINCREMENT, uuid TEXT, link TEXT, folder_type INTEGER)");
        db.execSQL("CREATE TABLE saved_photos(_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, photo INTEGER, FOREIGN KEY(user_id) REFERENCES users(_id)," +
                " FOREIGN KEY(photo) REFERENCES s_photos(_id))");
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
