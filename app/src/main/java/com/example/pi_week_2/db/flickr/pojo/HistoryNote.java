package com.example.pi_week_2.db.flickr.pojo;

import androidx.annotation.NonNull;

public class HistoryNote {
    private int id;
    private String text;
    private int userCode;

    public HistoryNote(int id, String text, int userCode) {
        this.id = id;
        this.text = text;
        this.userCode = userCode;
    }

    @NonNull
    @Override
    public String toString() {
        return text + "\n";
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getUserCode() {
        return userCode;
    }
}
