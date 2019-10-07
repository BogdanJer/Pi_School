package com.example.beermir;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.beermir.model.BreweryFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import network.BreweryApi;
import network.DataBreweryBeer;
import network.entity.beer.Beer;
import retrofit2.Response;

public class BreweryBeerFindAsync extends AsyncTask<String,Void, List<Beer>> {
    private Context context;
    private BreweryApi api;
    private BreweryFragment.BreweryBeerAdapter adapter;
    private ProgressBar progressBar;

    public BreweryBeerFindAsync(Context context, BreweryApi api, BreweryFragment.BreweryBeerAdapter adapter, ProgressBar progressBar){
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
    protected List<Beer> doInBackground(String...strings) {
        List<Beer> list = new ArrayList<>();
        try {
            Response<DataBreweryBeer> response = api.getBreweryBeers(strings[0],context.getString(R.string.sandbox_key)).execute();

            if(!response.isSuccessful()){
                cancel(true);
                return list;
            }

            for(Beer b : response.body().getData()){
                list.add(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<Beer> beers) {
        adapter.addItems(beers);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCancelled() {
        adapter.removeItems();
        progressBar.setVisibility(View.GONE);
    }
}
