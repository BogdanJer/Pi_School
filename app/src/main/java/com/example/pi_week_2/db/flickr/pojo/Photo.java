package com.example.pi_week_2.db.flickr.pojo;

import androidx.annotation.NonNull;

public class Photo {
    private int id;
    private String link;
    private int tagCode;

    public Photo(int id, String link, int tagCode) {
        this.id = id;
        this.link = link;
        this.tagCode = tagCode;
    }

    public int getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public int getTagCode() {
        return tagCode;
    }

    @NonNull
    @Override
    public String toString() {
        return "ID: " + id + "\nLink: " + link + "\nTag code: " + tagCode;
    }
}
