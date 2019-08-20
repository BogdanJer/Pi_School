package com.example.pi_week_2;

public class Photo {
    private String link;
    private String tag;

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

}
