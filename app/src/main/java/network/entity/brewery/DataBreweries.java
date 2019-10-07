package network.entity.brewery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataBreweries {
    @SerializedName("currentPage")
    @Expose
    private int currentPage;
    @SerializedName("numberOfPages")
    @Expose
    private int numberOfPages;
    @SerializedName("totalResults")
    @Expose
    private int totalResults;
    @SerializedName("data")
    @Expose
    private List<Brewery> data = null;
    @SerializedName("status")
    @Expose
    private String status;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Brewery> getData() {
        return data;
    }

    public void setData(List<Brewery> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
