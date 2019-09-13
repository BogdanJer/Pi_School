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
    private static final String PHOTO_TABLE = "photos";
    private static final String USER_FAVORITES_TABLE = "user_favorites";
    private static final String HISTORY_TABLE = "history";
    private static final String SAVED_PHOTOS = "s_photos";
    private static final String SAVED_PHOTOS_OONNECTION = "saved_photos";

    private static DbHelper dbHelper;
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

        Cursor c = dbHelper.getReadableDatabase().query(USER_TABLE, null, null, null, null, null, null);

        while (c.moveToNext()) {
            list.add(new User(c.getInt(0), c.getString(1)));
        }

        return list;
    }

    // Get all photos
    public List<Photo> getPhotos() {
        List<Photo> list = new ArrayList<>();

        Cursor c = dbHelper.getReadableDatabase().query(PHOTO_TABLE, null, null, null, null, null, null);

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

        Cursor c = dbHelper.getReadableDatabase().query(USER_TABLE, new String[]{"_id"}, "name=?", new String[]{name}, null, null, null);
        c.moveToFirst();

        if (c.getCount() != 0)
            id = c.getInt(0);

        return id;
    }

    // Insert new user
    public boolean insertUser(String name) {

        if (getUserId(name) == -1) {
            ContentValues values = new ContentValues();
            values.put("name", name);
            dbHelper.getWritableDatabase().insert(USER_TABLE, null, values);
            return true;
        }
        return false;
    }

    // Insert new photo
    public void insertPhoto(String userName, String tag, String link) {
        int photoId = getPhotoId(link);

        ContentValues contentValues = new ContentValues();

        // If photo isn't in database
        if (photoId == -1) {
            contentValues.put("link", link);
            contentValues.put("tag", tag);

            dbHelper.getWritableDatabase().insert(PHOTO_TABLE, null, contentValues);
            contentValues.clear();

            contentValues.put("user", getUserId(userName));
            contentValues.put("photo", getPhotoId(link));

        } else {  // If photo is exist add connection
            contentValues.put("user", getUserId(userName));
            contentValues.put("photo", photoId);
        }
        dbHelper.getWritableDatabase().insert(USER_FAVORITES_TABLE, null, contentValues);
    }

    public int getPhotoId(String link) {
        Cursor cursor = dbHelper.getReadableDatabase().query(PHOTO_TABLE, new String[]{"_id"}, "link=?", new String[]{link}, null, null, null);

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

        dbHelper.getWritableDatabase().insert(HISTORY_TABLE, null, values);
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
            dbHelper.getWritableDatabase().delete(PHOTO_TABLE, "link=?", new String[]{link});
    }

    // Save new photo to internal|external storage
    public void savePhoto(String user, String url, String uuid, int folderType) {
        int userId = getUserId(user);

        ContentValues values = new ContentValues();

        if (!isPhotoSaved(url)) {
            values.put("uuid", uuid);
            values.put("link", url);
            values.put("folder_type", folderType);
            dbHelper.getWritableDatabase().insert(SAVED_PHOTOS, null, values);
        }

        values.clear();
        if (getSavedPhoto(user, url) == -1) {
            values.put("user_id", userId);
            values.put("photo", getSavedPhotoId(url));

            dbHelper.getWritableDatabase().insert(SAVED_PHOTOS_OONNECTION, null, values);
        }

    }

    public int getSavedPhotoId(String url) {
        Cursor cursor = dbHelper.getReadableDatabase().query(SAVED_PHOTOS, new String[]{"_id"}, "link=?", new String[]{url}, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() != 0)
            return cursor.getInt(0);

        return -1;
    }

    public int getSavedPhoto(String user, String url) {
        int userId = getUserId(user);
        int photoId = getSavedPhotoId(url);

        Cursor cursor = dbHelper.getReadableDatabase().query(SAVED_PHOTOS_OONNECTION, new String[]{"_id"}, "user_id=? AND photo=?",
                new String[]{String.valueOf(userId), String.valueOf(photoId)}, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() != 0)
            return cursor.getInt(0);

        return -1;
    }

    public boolean isPhotoSaved(String url) {
        Cursor cursor = dbHelper.getReadableDatabase().query(SAVED_PHOTOS, new String[]{"_id"}, "link=?", new String[]{url}, null, null, null);

        if (cursor.getCount() != 0)
            return true;
        return false;
    }

    public void deleteSavedPhoto(String user, String url) {
        int userId = getUserId(user);
        int photoId = getSavedPhotoId(url);

        dbHelper.getWritableDatabase().delete(SAVED_PHOTOS_OONNECTION, "user_id=? AND photo=?", new String[]{String.valueOf(userId), String.valueOf(photoId)});

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT photo FROM " + SAVED_PHOTOS_OONNECTION + " INNER JOIN " + SAVED_PHOTOS + " ON " + SAVED_PHOTOS_OONNECTION +
                ".photo = " + SAVED_PHOTOS + "._id WHERE " + SAVED_PHOTOS + ".link = '" + url + "'", null);

        if (cursor.getCount() == 0)
            dbHelper.getWritableDatabase().delete(SAVED_PHOTOS, "link=?", new String[]{url});
    }

    public List<String> getUsersSavedPhotos(String user, int folderType) {
        List<String> list = new ArrayList<>();

        int userId = getUserId(user);

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT " + SAVED_PHOTOS + ".uuid FROM " + SAVED_PHOTOS_OONNECTION + " INNER JOIN " + SAVED_PHOTOS
                + " ON " + SAVED_PHOTOS + "._id = " + SAVED_PHOTOS_OONNECTION + ".photo WHERE " + SAVED_PHOTOS_OONNECTION + ".user_id = '" + userId + "' AND "
                + SAVED_PHOTOS + ".folder_type = '" + folderType + "'", null);

        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        return list;
    }
}
