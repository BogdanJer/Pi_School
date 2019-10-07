package com.example.beermir;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.beermir.model.BeerSearchFragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import network.BreweryApi;
import network.entity.beer.Beer;
import network.entity.beer.DataBeers;
import retrofit2.Response;

public class BeerFindAsync extends AsyncTask<String,Void, List<Beer>> {
    private ProgressBar progressBar;
    private BeerSearchFragment.BeerAdapter adapter;
    private BreweryApi api;
    private Context context;

    public BeerFindAsync(Context context, BreweryApi api, BeerSearchFragment.BeerAdapter adapter, ProgressBar progressBar){
        this.context = context;
        this.api = api;
        this.adapter = adapter;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<Beer> doInBackground(final String...texts) {
        List<Beer> list = new ArrayList<>();
        try {
            Response<DataBeers> response = api.getAllBeers("1",context.getString(R.string.sandbox_key)).execute();

            if(!response.isSuccessful()){
                Log.e("Get response","Unsuccessful");
                cancel(true);
                return null;
            }

            for (Beer b : response.body().getData()) {
                if (isCancelled()) {
                    return null;
                }
                if (b.getName().toLowerCase().contains(texts[0].toLowerCase())) {
                    list.add(b);
                }
            }
            list.addAll(getAllPages(response,texts[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
    private List<Beer> getAllPages(Response<DataBeers> fResponse, final String text){
        List<Beer> list = new ArrayList<>();

        if(fResponse.body().getNumberOfPages()>1) {
            for(int i=2;i<fResponse.body().getNumberOfPages();i++) {
                try {
                    Response<DataBeers> response = api.getAllBeers(String.valueOf(i), context.getString(R.string.sandbox_key)).execute();

                    if (!response.isSuccessful()) {
                        Log.e("Get response","Unsuccessful");
                        cancel(true);
                        return null;
                    }

                    for (Beer b : response.body().getData()) {
                        if (isCancelled())
                            return null;

                        if (b.getName().toLowerCase().contains(text.toLowerCase())) {
                            list.add(b);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<Beer> list) {
        progressBar.setVisibility(View.GONE);
        adapter.addItems(list);
    }

    @Override
    protected void onCancelled() {
        adapter.removeItems();
        progressBar.setVisibility(View.GONE);
    }
}
