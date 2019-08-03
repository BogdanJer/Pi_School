package com.example.pi_week_2.Async;

import android.os.AsyncTask;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pi_week_2.Flickr.FlickrManager;

import java.lang.ref.WeakReference;


public class FindPhotosAsync extends AsyncTask<Void, Void, String> {
    private final String searchWord;
    private WeakReference<TextView> linksListView;
    private WeakReference<ProgressBar> pbWeakRef;

    public FindPhotosAsync(TextView searchText, TextView linksListView, ProgressBar progressBar) {
        this.linksListView = new WeakReference<>(linksListView);
        searchWord = searchText.getText().toString();
        pbWeakRef = new WeakReference<>(progressBar);
    }

    @Override
    protected void onPreExecute() {
        pbWeakRef.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... voids) {

        FlickrManager fm = FlickrManager.getInstance();
        if (isCancelled())
            return null;

        return fm.searchPhotos(searchWord);
    }

    @Override
    protected void onPostExecute(String links) {
        pbWeakRef.get().setVisibility(View.GONE);
        linksListView.get().setText(links);
        Linkify.addLinks(linksListView.get(), Linkify.WEB_URLS);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i("Search photo thread", "Cancel");
    }
}