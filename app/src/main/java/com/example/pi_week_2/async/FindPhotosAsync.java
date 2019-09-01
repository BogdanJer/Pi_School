package com.example.pi_week_2.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.pi_week_2.adapter.MapPhotosAdapter;
import com.example.pi_week_2.adapter.PhotoAdapter;
import com.example.pi_week_2.flickr.FlickrManager;

import java.lang.ref.WeakReference;
import java.util.List;

public class FindPhotosAsync extends AsyncTask<Void, Void, List<String>> {
    private String tag;
    private WeakReference<ProgressBar> pbWR;
    private WeakReference<PhotoAdapter> adapterWR;
    private WeakReference<MapPhotosAdapter> mapPhotosAdapterWR;

    private double latitude;
    private double longitude;

    private FlickrManager.FlickrSearchWays way;

    public FindPhotosAsync(ProgressBar progressBar, PhotoAdapter adapter, String tag) {
        this.tag = tag;
        pbWR = new WeakReference<>(progressBar);
        adapterWR = new WeakReference<>(adapter);
        way = FlickrManager.FlickrSearchWays.TAG;
    }

    public FindPhotosAsync(ProgressBar progressBar, MapPhotosAdapter adapter, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        pbWR = new WeakReference<>(progressBar);
        mapPhotosAdapterWR = new WeakReference<>(adapter);
        way = FlickrManager.FlickrSearchWays.LAT_LON;
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

        switch (way) {
            case LAT_LON:
                return fm.searchPhotos().byLatLon(latitude, longitude).getPhotoList();
            default:
                return fm.searchPhotos().byTag(tag).getPhotoList();
        }
    }

    @Override
    protected void onPostExecute(List<String> links) {
        if (pbWR.get() != null) {
            pbWR.get().setVisibility(View.GONE);

            switch (way) {
                case TAG:
                    adapterWR.get().setLinks(links, tag);
                    break;
                case LAT_LON:
                    mapPhotosAdapterWR.get().setLinks(links, latitude, longitude);
            }

        } else
            Log.e("Search photos", "Error! Null pointer");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i("Search photo thread", "Cancel");
    }
}