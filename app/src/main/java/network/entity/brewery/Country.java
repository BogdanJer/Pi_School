package network.entity.brewery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("isoCode")
    @Expose
    private String isoCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("isoThree")
    @Expose
    private String isoThree;
    @SerializedName("numberCode")
    @Expose
    private int numberCode;
    @SerializedName("createDate")
    @Expose
    private String createDate;

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIsoThree() {
        return isoThree;
    }

    public void setIsoThree(String isoThree) {
        this.isoThree = isoThree;
    }

    public int getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(int numberCode) {
        this.numberCode = numberCode;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}