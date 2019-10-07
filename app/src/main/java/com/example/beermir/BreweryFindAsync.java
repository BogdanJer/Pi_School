package com.example.beermir;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.beermir.model.BreweryLocationFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import network.BreweryApi;
import network.entity.brewery.Brewery;
import network.entity.brewery.DataBreweries;
import network.entity.brewery.Location;
import retrofit2.Response;

public class BreweryFindAsync extends AsyncTask<Double,Void, List<Brewery>> {
    private Context context;
    private BreweryApi api;
    private BreweryLocationFragment.BreweryAdapter adapter;
    private ProgressBar progressBar;

    public BreweryFindAsync(Context context, BreweryApi api, BreweryLocationFragment.BreweryAdapter adapter, ProgressBar progressBar){
        this.context = context;
        this.api = api;
        this.adapter = adapter;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<Brewery> doInBackground(final Double...doubles) {
        List<Brewery> breweries = new ArrayList<>();
        try {
            Response<DataBreweries> response = api.getAllBreweriesWithLocation("Y",context.getString(R.string.sandbox_key)).execute();

            if (response.isSuccessful()) {
                for (Brewery b : response.body().getData()) {
                    if (isCancelled()) {
                        return null;
                    }
                    if (b.getLocations() != null)
                        for (Location l : b.getLocations())
                            if (Math.abs(l.getLatitude() - doubles[0]) < 1 && Math.abs(l.getLongitude() - doubles[1]) < 1) {
                                breweries.add(b);
                            }
                }
            }else{
                cancel(true);
                Log.e("Get response","Failure");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return breweries;
    }

    @Override
    protected void onPostExecute(List<Brewery> breweries) {
        progressBar.setVisibility(View.GONE);
        adapter.addBreweries(breweries);
    }

    @Override
    protected void onCancelled() {
        progressBar.setVisibility(View.GONE);
        adapter.removeItems();
    }
}
