package com.example.pi_week_2.db.flickr;

import android.graphics.Bitmap;

public class Image {
    private String path;
    private Bitmap photo;
    private long timeOfModified;

    public Image(String path, Bitmap photo, long timeOfModified) {
        this.path = path;
        this.photo = photo;
        this.timeOfModified = timeOfModified;
    }

    public String getPath() {
        return path;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public long getTimeOfModified() {
        return timeOfModified;
    }
}
