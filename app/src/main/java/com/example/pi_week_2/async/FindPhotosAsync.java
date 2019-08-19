package com.example.pi_week_2.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.pi_week_2.PhotoSearchRecyclerView;
import com.example.pi_week_2.flickr.FlickrManager;

import java.lang.ref.WeakReference;
import java.util.List;

public class FindPhotosAsync extends AsyncTask<Void, Void, List<String>> {
    private final String searchWord;
    private WeakReference<ProgressBar> pbWR;
    private PhotoSearchRecyclerView photoSearchRecyclerView;
    private Context context;

    public FindPhotosAsync(Context context, ProgressBar progressBar, PhotoSearchRecyclerView photoSearchRecyclerView, String tag) {
        this.photoSearchRecyclerView = photoSearchRecyclerView;
        searchWord = tag;
        pbWR = new WeakReference<>(progressBar);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if (pbWR.get() != null)
            pbWR.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected List<String> doInBackground(Void... voids) {

        FlickrManager fm = FlickrManager.getInstance();

        if (isCancelled())
            return null;

        return fm.searchPhotos(searchWord);
    }

    @Override
    protected void onPostExecute(List<String> links) {
        if (pbWR.get() != null) {

            photoSearchRecyclerView.getLinks(links);
            //pbWR.get().setVisibility(View.GONE);
        } else
            Log.e("Search photos", "Error! Null pointer");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i("Search photo thread", "Cancel");
    }

}