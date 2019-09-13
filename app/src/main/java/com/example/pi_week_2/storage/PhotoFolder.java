package com.example.pi_week_2.storage;

import android.graphics.Bitmap;

import com.example.pi_week_2.db.flickr.Image;

import java.util.List;

public interface PhotoFolder {
    int EXTERNAL_FOLDER = 1;
    int INTERNAL_FOLDER = 2;

    String savePhoto(Bitmap photo, String url);

    boolean deletePhoto(String fileName);

    List<Image> getPhotos(String user);

    Bitmap getPhoto(String path);
}
