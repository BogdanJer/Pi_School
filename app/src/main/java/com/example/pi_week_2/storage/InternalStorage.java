package com.example.pi_week_2.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.pi_week_2.db.flickr.FlickrDAO;
import com.example.pi_week_2.db.flickr.Image;
import com.example.pi_week_2.model.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class InternalStorage implements PhotoFolder {
    public static final String LOCAL_PHOTOS_STORAGE_TAG = "Create photos folder";
    public static final String SAVE_PHOTO = "Saving photo";
    public static final String NO_PHOTOS = "No photos";
    public static final String DELETE_PHOTO = "Photo deleting";


    private final String PHOTO_FOLDER = "Photos";

    private File photosFolder;
    private Context context;

    public InternalStorage(Context context) {
        this.context = context;

        photosFolder = context.getDir(PHOTO_FOLDER, MODE_PRIVATE);

        if (!photosFolder.exists()) {
            if (!photosFolder.mkdirs()) {
                Log.e(LOCAL_PHOTOS_STORAGE_TAG, "Could not create photos folder");
            }
        }
    }

    @Override
    public String savePhoto(Bitmap photo, String url) {
        String uuid = UUID.randomUUID().toString();

        String fileName = uuid + ".png";

        File file = new File(photosFolder, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
            FlickrDAO.getDao(context).savePhoto(MainActivity.name, url, uuid, INTERNAL_FOLDER);
            Log.i("Photo", "Is saved!");
        } catch (IOException e) {
            Log.e(SAVE_PHOTO, "Photo saving error!");
            return null;
        }

        return file.getAbsolutePath();
    }

    @Override
    public boolean deletePhoto(String fileName) {
        File file = new File(fileName);
        if (!file.delete()) {
            Log.e(DELETE_PHOTO, "Photo could not be deleted!");
            return false;
        }
        return true;
    }

    @Override
    public List<Image> getPhotos(String user) {
        List<String> photoList = FlickrDAO.getDao(context).getUsersSavedPhotos(MainActivity.name, INTERNAL_FOLDER);

        File[] photos = photosFolder.listFiles((dir, name) -> {
            if (photoList.contains(name.substring(0, name.lastIndexOf(".png"))))
                return true;
            return false;
        });

        List<Image> list = new ArrayList<>(photos.length);
        for (File f : photos) {
            list.add(new Image(f.getAbsolutePath(), BitmapFactory.decodeFile(f.getAbsolutePath()), f.lastModified()));
            System.out.println(f.lastModified());
        }

        return list;
    }

    @Override
    public Bitmap getPhoto(String path) {
        if (!(new File(path).exists()))
            return null;

        return BitmapFactory.decodeFile(path);
    }
}
