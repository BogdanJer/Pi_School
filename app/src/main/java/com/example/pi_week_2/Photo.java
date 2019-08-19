package com.example.pi_week_2;

import android.graphics.Bitmap;

public class Photo {
    private String link;
    private String tag;
    private Bitmap photo;

    public Photo(String link, String tag) {
        this.link = link;
        this.tag = tag;
    }

    public String getLink() {
        return link;
    }

    public String getTag() {
        return tag;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
