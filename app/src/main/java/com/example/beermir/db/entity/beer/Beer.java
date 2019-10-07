package com.example.beermir.db.entity.beer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "beer")
public class Beer {
    @PrimaryKey
    @NonNull
    private String id;

    public String getId() {
     return id;
    }

    public void setId(String id) {
     this.id = id;
    }

 /**private String name;

 private String nameDisplay;

 private int glassWareId;

 private String labelsId;

 private int styleId;

 private String isOrganic;

 private String isRetired;

 private String status;

 private String statusDisplay;

 private String createDate;

 private String updateDate;


public String getName() {
return name;
}

public String getNameDisplay() {
return nameDisplay;
}

public int getGlassWareId() {
return glassWareId;
}

public String getIsOrganic() {
return isOrganic;
}

public String getIsRetired() {
return isRetired;
}

public String getStatus() {
return status;
}

public String getStatusDisplay() {
return statusDisplay;
}

public String getCreateDate() {
return createDate;
}

public String getUpdateDate() {
return updateDate;
}


public void setName(String name) {
this.name = name;
}

public void setNameDisplay(String nameDisplay) {
this.nameDisplay = nameDisplay;
}

public void setGlassWareId(int glassWareId) {
this.glassWareId = glassWareId;
}

public String getLabelsId() {
return labelsId;
}

public void setLabelsId(String labelsId) {
this.labelsId = labelsId;
}

public int getStyleId() {
return styleId;
}

public void setStyleId(int styleId) {
this.styleId = styleId;
}

public void setIsOrganic(String isOrganic) {
this.isOrganic = isOrganic;
}

public void setIsRetired(String isRetired) {
this.isRetired = isRetired;
}

public void setStatus(String status) {
this.status = status;
}

public void setStatusDisplay(String statusDisplay) {
this.statusDisplay = statusDisplay;
}

public void setCreateDate(String createDate) {
this.createDate = createDate;
}

public void setUpdateDate(String updateDate) {
this.updateDate = updateDate;
}
  */
}


/**
 *
 * BEER
 @SerializedName("id")
 @Expose
 private String id;
 @SerializedName("name")
 @Expose
 private String name;
 @SerializedName("nameDisplay")
 @Expose
 private String nameDisplay;
 @SerializedName("abv")
 @Expose
 private String abv;
 @SerializedName("glasswareId")
 @Expose
 private int glasswareId;
 @SerializedName("styleId")
 @Expose
 private int styleId;
 @SerializedName("isOrganic")
 @Expose
 private String isOrganic;
 @SerializedName("isRetired")
 @Expose
 private String isRetired;

 public String getId() {
 return id;
 }

 public void setId(String id) {
 this.id = id;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public String getNameDisplay() {
 return nameDisplay;
 }

 public void setNameDisplay(String nameDisplay) {
 this.nameDisplay = nameDisplay;
 }

 public String getAbv() {
 return abv;
 }

 public void setAbv(String abv) {
 this.abv = abv;
 }

 public int getGlasswareId() {
 return glasswareId;
 }

 public void setGlasswareId(int glasswareId) {
 this.glasswareId = glasswareId;
 }

 public int getStyleId() {
 return styleId;
 }

 public void setStyleId(int styleId) {
 this.styleId = styleId;
 }

 public String getIsOrganic() {
 return isOrganic;
 }

 public void setIsOrganic(String isOrganic) {
 this.isOrganic = isOrganic;
 }

 public String getIsRetired() {
 return isRetired;
 }

 public void setIsRetired(String isRetired) {
 this.isRetired = isRetired;
 }

 }
 */

/**
 Labels

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

 */

/**

 */