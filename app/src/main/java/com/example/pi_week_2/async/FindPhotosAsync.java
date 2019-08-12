package com.example.pi_week_2.async;

import android.os.AsyncTask;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pi_week_2.flickr.FlickrManager;

public class FindPhotosAsync extends AsyncTask<Void, Void, String> {
    private final String searchWord;
    private TextView linksListView;
    private ProgressBar pb;

    public FindPhotosAsync(TextView searchText, TextView linksListView, ProgressBar progressBar) {
        this.linksListView = linksListView;
        searchWord = searchText.getText().toString();
        pb = progressBar;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
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
        pb.setVisibility(View.GONE);
        linksListView.setText(links);
        Linkify.addLinks(linksListView, Linkify.WEB_URLS);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i("Search photo thread", "Cancel");
    }
}