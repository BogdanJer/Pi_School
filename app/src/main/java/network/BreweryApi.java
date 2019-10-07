package network;

import network.entity.beer.DataBeers;
import network.entity.brewery.DataBreweries;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BreweryApi {
    @GET("beers/?")
    Call<DataBeers> getAllBeers(@Query("p") String page, @Query("key") String key);

    @GET("breweries/")
    Call<DataBreweries> getAllBreweriesWithLocation(@Query("withLocations") String withLocation, @Query("key") String key);

    @GET("beer/{beerId}")
    Call<DataBeerById> getBeerById(@Path("beerId") String beerId, @Query("key") String key);

    @GET("brewery/{breweryId}/beers")
    Call<DataBreweryBeer> getBreweryBeers(@Path("breweryId") String breweryId, @Query("key") String key);
}
