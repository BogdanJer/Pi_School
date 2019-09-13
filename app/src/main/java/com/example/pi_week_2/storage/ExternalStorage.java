package com.example.pi_week_2.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.pi_week_2.R;
import com.example.pi_week_2.db.flickr.FlickrDAO;
import com.example.pi_week_2.db.flickr.Image;
import com.example.pi_week_2.model.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExternalStorage implements PhotoFolder {
    public static final String PHOTO_FOLDER = "photos";
    private final String EXTERNAL_STORAGE = "External storage";
    private Context context;
    private File photoFolder;

    public ExternalStorage(Context context) {
        this.context = context;

        if (!isExternalStorageReadable()) {
            Toast.makeText(context, "Storage isn't available!", Toast.LENGTH_LONG).show();
            Log.e(EXTERNAL_STORAGE, "Storage isn't available!");
            return;
        }

        File appFolder = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name));

        if (!appFolder.exists()) {
            if (!appFolder.mkdirs()) {
                Log.e("Creating folder", "Parent folder doesn't create");
                return;
            }
        }

        photoFolder = new File(appFolder.getAbsolutePath(), PHOTO_FOLDER);

        if (!photoFolder.exists()) {
            if (!photoFolder.mkdirs()) {
                Log.e("Creating folder", "Error");
            }
        }
    }

    @Override
    public String savePhoto(Bitmap photo, String url) {
        if (!isExternalStorageWritable()) {
            Log.e(EXTERNAL_STORAGE, "External storage isn't writable");
            return null;
        }

        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ".png";

        File file = new File(photoFolder, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            photo.compress(Bitmap.CompressFormat.PNG, 100, fos);

            FlickrDAO.getDao(context).savePhoto(MainActivity.name, url, uuid, EXTERNAL_FOLDER);
            fos.flush();
            Log.i("External storage/Photo", "is saved");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return "";
    }

    @Override
    public boolean deletePhoto(String fileName) {
        File photo = new File(fileName);

        if (!photo.delete()) {
            Log.e(EXTERNAL_STORAGE, "Photo could not be deleted!");
            return false;
        }
        return true;
    }

    @Override
    public List<Image> getPhotos(String user) {
        List<String> photoList = FlickrDAO.getDao(context).getUsersSavedPhotos(user, EXTERNAL_FOLDER);

        for (String s : photoList)
            System.out.println("PHOTO LIST: " + s);

        File[] photos = photoFolder.listFiles((dir, name) -> {
            if (photoList.contains(name.substring(0, name.lastIndexOf(".png"))))
                return true;
            return false;
        });

        List<Image> list = new ArrayList<>(photos.length);
        for (File f : photos) {
            list.add(new Image(f.getAbsolutePath(), BitmapFactory.decodeFile(f.getAbsolutePath()), f.lastModified()));
            System.out.println("EXTERNAL: " + f.lastModified());
        }
        return list;
    }

    @Override
    public Bitmap getPhoto(String path) {
        File file = new File(photoFolder, path);

        if (file.exists())
            return BitmapFactory.decodeFile(file.getAbsolutePath());

        return null;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }

    public File getPhotoFolder() {
        return photoFolder;
    }
}
