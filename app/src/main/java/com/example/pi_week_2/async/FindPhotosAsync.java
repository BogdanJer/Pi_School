package com.example.pi_week_2.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.pi_week_2.adapter.PhotoAdapter;
import com.example.pi_week_2.flickr.FlickrManager;

import java.lang.ref.WeakReference;
import java.util.List;

public class FindPhotosAsync extends AsyncTask<Void, Void, List<String>> {
    private final String tag;
    private WeakReference<ProgressBar> pbWR;
    private WeakReference<PhotoAdapter> adapterWR;

    public FindPhotosAsync(ProgressBar progressBar, PhotoAdapter adapter, String tag) {
        this.tag = tag;
        pbWR = new WeakReference<>(progressBar);
        adapterWR = new WeakReference<>(adapter);

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

        return fm.searchPhotos(tag);
    }

    @Override
    protected void onPostExecute(List<String> links) {
        if (pbWR.get() != null && adapterWR.get() != null) {
            pbWR.get().setVisibility(View.GONE);
            adapterWR.get().setLinks(links, tag);
        } else
            Log.e("Search photos", "Error! Null pointer");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i("Search photo thread", "Cancel");
    }

}