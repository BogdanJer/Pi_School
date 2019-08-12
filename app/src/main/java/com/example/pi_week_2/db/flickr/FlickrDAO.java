package com.example.pi_week_2.db.flickr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.pi_week_2.db.flickr.pojo.HistoryNote;
import com.example.pi_week_2.db.flickr.pojo.Photo;
import com.example.pi_week_2.db.flickr.pojo.Tag;
import com.example.pi_week_2.db.flickr.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class FlickrDAO {
    private static final FlickrDAO dao = new FlickrDAO();
    private static DbHelper dbHelper;

    public static FlickrDAO getDao(Context context) {
        dbHelper = new DbHelper(context);
        return dao;
    }

    public static DbHelper getDbHelper() {
        return dbHelper;
    }

    // Get all users
    public List<User> getUsers() {
        List<User> list = new ArrayList<>();

        Cursor c = dbHelper.getReadableDatabase().query("users", null, null, null, null, null, null);

        while (c.moveToNext()) {
            list.add(new User(c.getInt(0), c.getString(1)));
        }

        return list;
    }

    // Get all tags
    public List<Tag> getTags() {
        List<Tag> list = new ArrayList<>();

        Cursor c = dbHelper.getReadableDatabase().query("tags", null, null, null, null, null, null);

        while (c.moveToNext()) {
            list.add(new Tag(c.getInt(0), c.getString(1), c.getInt(2)));
        }

        return list;
    }

    // Get all photos
    public List<Photo> getPhotos() {
        List<Photo> list = new ArrayList<>();

        Cursor c = dbHelper.getReadableDatabase().query("photos", null, null, null, null, null, null);

        while (c.moveToNext()) {
            list.add(new Photo(c.getInt(0), c.getString(1), c.getInt(2)));
        }
        return list;
    }

    public List<HistoryNote> getUserHistory(String userName) {
        List<HistoryNote> list = new ArrayList<>();

        Cursor c = dbHelper.getReadableDatabase().query("history", null, null, null, null, null, null);

        while (c.moveToNext()) {
            list.add(new HistoryNote(c.getInt(0), c.getString(1), c.getInt(2)));
        }
        return list;
    }

    // Get user id found by name
    public int getUserId(String name) {
        int id = -1;

        Cursor c = dbHelper.getReadableDatabase().query("users", new String[]{"_id"}, "name=?", new String[]{name}, null, null, null);
        c.moveToFirst();

        if (c.getCount() != 0)
            id = c.getInt(0);

        return id;
    }

    // Get tag id found by title
    public int getUserTagId(String userName, String title) {
        int id = -1;

        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT tags._id FROM tags INNER JOIN users ON tags.user_code = users._id " +
                "WHERE users.name='" + userName + "' AND title='" + title + "'", null);

        c.moveToFirst();
        if (c.getCount() != 0)
            id = c.getInt(0);

        return id;
    }

    // Insert new user
    public boolean insertUser(String name) {

        if (getUserId(name) == -1) {
            dbHelper.getWritableDatabase().execSQL("INSERT INTO users (name) VALUES ('" + name + "')");
            return true;
        }
        return false;
    }

    // Insert new tag
    public boolean insertTag(String userName, String title) {

        if (getUserTagId(userName, title) == -1) {
            int userId = getUserId(userName);
            dbHelper.getWritableDatabase().execSQL("INSERT INTO tags (title,user_code) VALUES ('" + title + "','" + userId + "')");
            return true;
        }
        return false;
    }

    // Insert new photo
    public boolean insertPhoto(String userName, String tagTitle, String link) {

        int tagId = getUserTagId(userName, tagTitle);
        dbHelper.getWritableDatabase().execSQL("INSERT INTO photos (link,tag_code) VALUES ('" + link + "','" + tagId + "')");

        return true;
    }

    // Insert new history note
    public boolean insertHistoryNote(String user, String text) {
        int userId = getUserId(user);

        //dbHelper.getWritableDatabase().execSQL("INSERT INTO history (text, user_code) VALUES ('" + text + "','" + userId + "')");

        ContentValues values = new ContentValues();
        values.put("text", text);
        values.put("user_code", userId);

        long a = dbHelper.getWritableDatabase().insert("history", null, values);

        if (a != 0)
            return true;

        return false;
    }

    // Get user's favorite
    public String getUserFavorite(String name) {
        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT DISTINCT(tags.title), photos.link FROM users INNER JOIN tags ON tags.user_code=users._id " +
                "INNER JOIN photos ON tags._id=photos.tag_code WHERE users.name='" + name + "'", null);

        StringBuffer str = new StringBuffer();

        while (c.moveToNext()) {
            str.append(c.getString(0) + "\n" + c.getString(1) + "\n");
        }

        return str.toString();
    }

    // Get user's tags by search word
    public boolean isUserTagExist(String userName, String searchWord) {
        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT tags._id FROM tags INNER JOIN users ON users._id = tags.user_code WHERE users.name = '" + userName +
                "' AND tags.title = '" + searchWord + "'", null);

        c.moveToFirst();

        if (c.getCount() != 0)
            return true;
        return false;
    }

    public boolean userHasPhoto(String user, String tag, String link) {

        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT photos._id FROM photos INNER JOIN tags ON photos.tag_code=tags._id INNER JOIN " +
                "users ON tags.user_code=users._id WHERE users.name='" + user + "' AND tags.title='" + tag + "' AND photos.link='" + link + "'", null);

        c.moveToFirst();

        if (c.getCount() != 0)
            return true;

        return false;
    }

    public boolean deletePhoto(String user, String tag, String link) {
        //dbHelper.getWritableDatabase().execSQL("DELETE FROM photos INNER JOIN tags ON photos.tag_code = tags._id INNER JOIN users ON tags.user_code = users._id" +
        //      " WHERE users._id = tags.user_code AND tags._id = photos.tag_code " +
        //    "AND users.name='" + user + "' AND tags.title='" + tag + "' AND photos.link='" + link + "'");

        dbHelper.getWritableDatabase().delete("photos,users,tags", "INNER JOIN tags ON photos.tag_code = tags._id INNER JOIN users ON tags.user_code = users._id " +
                " WHERE users._id = tags.user_code AND tags._id = photos.tag_code AND users.name=? AND tags.title=? AND photos.link=?", new String[]{user, tag, link});
        return true;
    }
}
