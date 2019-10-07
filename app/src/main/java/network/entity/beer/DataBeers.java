package network.entity.beer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataBeers {
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
    private List<Beer> data = null;

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

    public List<Beer> getData() {
        return data;
    }

    public void setData(List<Beer> data) {
        this.data = data;
    }
}
