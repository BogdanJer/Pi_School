package com.example.pi_week_2.db.flickr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.pi_week_2.db.flickr.pojo.Photo;
import com.example.pi_week_2.db.flickr.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class FlickrDAO {
    private static final String USER_TABLE = "users";
    private static DbHelper dbHelper;
    private static final String PHOTO_TABLE = "photos";
    private static final String USER_FAVORITES_TABLE = "user_favorites";
    private static FlickrDAO dao;

    public static FlickrDAO getDao(Context context) {
        if (dao == null) {
            dao = new FlickrDAO();
            dbHelper = new DbHelper(context);
        }
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
   /* public List<Tag> getTags() {
        List<Tag> list = new ArrayList<>();

        Cursor c = dbHelper.getReadableDatabase().query("tags", null, null, null, null, null, null);

        while (c.moveToNext()) {
            list.add(new Tag(c.getInt(0), c.getString(1), c.getInt(2)));
        }

        return list;
    }*/

    // Get all photos
    public List<Photo> getPhotos() {
        List<Photo> list = new ArrayList<>();

        Cursor c = dbHelper.getReadableDatabase().query("photos", null, null, null, null, null, null);

        while (c.moveToNext()) {
            list.add(new Photo(c.getInt(0), c.getString(1), c.getInt(2)));
        }
        return list;
    }

    public List<String> getUserHistory(String userName) {
        List<String> list = new ArrayList<>();

        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT history.* FROM history INNER JOIN users ON history.user_id = users._id WHERE " +
                "users.name = '" + userName + "' ORDER BY history._id DESC LIMIT 20", null);

        while (c.moveToNext()) {
            list.add(c.getString(1));
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
    /*public int getUserTagId(String userName, String title) {
        int id = -1;

        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT tags._id FROM tags INNER JOIN users ON tags.user_code = users._id " +
                "WHERE users.name='" + userName + "' AND title='" + title + "'", null);

        c.moveToFirst();
        if (c.getCount() != 0)
            id = c.getInt(0);

        return id;
    }*/

    // Insert new user
    public boolean insertUser(String name) {

        if (getUserId(name) == -1) {
            dbHelper.getWritableDatabase().execSQL("INSERT INTO users (name) VALUES ('" + name + "')");
            return true;
        }
        return false;
    }

    // Insert new tag
    /*public boolean insertTag(String userName, String title) {

        if (getUserTagId(userName, title) == -1) {
            int userId = getUserId(userName);
            dbHelper.getWritableDatabase().execSQL("INSERT INTO tags (title,user_code) VALUES ('" + title + "','" + userId + "')");
            return true;
        }
        return false;
    }*/

    // Insert new photo
    public void insertPhoto(String userName, String tag, String link) {
        int photoId = getPhotoId(link);

        ContentValues contentValues = new ContentValues();

        // If photo isn't in database
        if (photoId == -1) {
            // contentValues.put("link",link);
            //contentValues.put("tag",tag);

            //dbHelper.getWritableDatabase().insert("photos",null,contentValues);
            dbHelper.getWritableDatabase().execSQL("INSERT INTO photos (link, tag) VALUES ('" + link + "','" + tag + "');");
            //contentValues.clear();

            contentValues.put("user", getUserId(userName));
            contentValues.put("photo", getPhotoId(link));

        } else {  // If photo is exist add connection
            contentValues.put("user", getUserId(userName));
            contentValues.put("photo", photoId);
        }
        dbHelper.getWritableDatabase().insert(USER_FAVORITES_TABLE, null, contentValues);
    }

    public int getPhotoId(String link) {
        Cursor cursor = dbHelper.getReadableDatabase().query("photos", new String[]{"_id"}, "link=?", new String[]{link}, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() != 0)
            return cursor.getInt(0);

        return -1;
    }

    // Insert new history note
    public void insertHistoryNote(String userName, String text) {
        int userId = getUserId(userName);

        ContentValues values = new ContentValues();
        values.put("text", text);
        values.put("user_id", userId);

        dbHelper.getWritableDatabase().insert("history", null, values);
    }

    // Get user's favorite
    public List<com.example.pi_week_2.Photo> getUserFavorite(String name) {
        int userId = getUserId(name);

        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT photos.link, photos.tag FROM " + PHOTO_TABLE + " INNER" +
                " JOIN " + USER_FAVORITES_TABLE + " ON photos._id = user_favorites.photo WHERE user_favorites.user='" + userId + "'", null);

        List<com.example.pi_week_2.Photo> list = new ArrayList<>(c.getCount());

        while (c.moveToNext())
            list.add(new com.example.pi_week_2.Photo(c.getString(0), c.getString(1)));

        return list;
    }

    // Get user's tags by search word
    /*public boolean isUserTagExist(String userName, String searchWord) {
        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT tags._id FROM tags INNER JOIN users ON users._id = tags.user_code WHERE users.name = '" + userName +
                "' AND tags.title = '" + searchWord + "'", null);

        c.moveToFirst();

        if (c.getCount() != 0)
            return true;
        return false;
    }*/

    public boolean userHasPhoto(String userName, String link) {
        int userId = getUserId(userName);

        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT COUNT(user_favorites.user) FROM user_favorites INNER JOIN " +
                "photos ON user_favorites.photo = photos._id WHERE user_favorites.user='" + userId + "' AND photos.link='" + link + "'", null);

        c.moveToFirst();

        if (c.getInt(0) != 0)
            return true;

        return false;
    }

    public void deletePhoto(String user, String link) {
        int userId = getUserId(user);
        int photoId = getPhotoId(link);

        dbHelper.getWritableDatabase().execSQL("DELETE FROM user_favorites WHERE user_favorites.photo = '" + photoId
                + "' AND user_favorites.user='" + userId + "'");

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT COUNT(user_favorites.user) FROM user_favorites INNER JOIN photos ON user_favorites.photo=photos._id" +
                " WHERE photos.link='" + link + "'", null);

        cursor.moveToFirst();
        if (cursor.getInt(0) == 0)
            dbHelper.getWritableDatabase().delete("photos", "link=?", new String[]{link});
    }
}
