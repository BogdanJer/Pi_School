package com.example.pi_week_2.db.flickr.pojo;

import androidx.annotation.NonNull;

public class Tag {
    private int id;
    private String title;
    private int userCode;

    public Tag(int id, String title, int userCode) {
        this.id = id;
        this.title = title;
        this.userCode = userCode;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getUserCode() {
        return userCode;
    }

    @NonNull
    @Override
    public String toString() {
        return "ID: " + id + "\nTitle: " + title + "\nUser code: " + userCode;
    }
}
