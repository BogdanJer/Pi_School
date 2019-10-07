package com.example.beermir.db.entity.beer;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "labels")
public class Labels {
    @PrimaryKey
    @NonNull
    private String id;

    private String icon;
    private String medium;
    private String large;

    private String contentAwareIcon;
    private String contentAwareMedium;
    private String contentAwareLarge;




    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getContentAwareIcon() {
        return contentAwareIcon;
    }

    public void setContentAwareIcon(String contentAwareIcon) {
        this.contentAwareIcon = contentAwareIcon;
    }

    public String getContentAwareMedium() {
        return contentAwareMedium;
    }

    public void setContentAwareMedium(String contentAwareMedium) {
        this.contentAwareMedium = contentAwareMedium;
    }

    public String getContentAwareLarge() {
        return contentAwareLarge;
    }

    public void setContentAwareLarge(String contentAwareLarge) {
        this.contentAwareLarge = contentAwareLarge;
    }
}
