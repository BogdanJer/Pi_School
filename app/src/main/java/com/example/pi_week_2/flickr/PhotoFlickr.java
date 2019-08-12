package com.example.pi_week_2.flickr;

import androidx.annotation.NonNull;

public class PhotoFlickr {
    private String id;
    private String owner;
    private String secret;
    private String server;
    private String farm;
    private String title;
    private boolean isPublic;
    private boolean isFriend;
    private boolean isFamily;

    public PhotoFlickr(String id, String owner, String secret, String server, String farm, String title, boolean isPublic, boolean isFriend, boolean isFamily) {
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.isPublic = isPublic;
        this.isFriend = isFriend;
        this.isFamily = isFamily;
    }

    @NonNull
    @Override
    public String toString() {
        return "id: " + id + "\nowner: " + owner + "\nsecret: " + secret + "\nserver: " + server + "\nfarm: " + farm + "\ntitle: " + title
                + "\nisPuplic: " + isPublic + "\nisFriend: " + isFriend + "\nisFamily: " + isFamily + "\n\n";
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public String getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public boolean isFamily() {
        return isFamily;
    }
}
