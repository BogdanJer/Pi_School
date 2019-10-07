package network.entity.beer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Labels {
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("large")
    @Expose
    private String large;
    @SerializedName("contentAwareIcon")
    @Expose
    private String contentAwareIcon;
    @SerializedName("contentAwareMedium")
    @Expose
    private String contentAwareMedium;
    @SerializedName("contentAwareLarge")
    @Expose
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
