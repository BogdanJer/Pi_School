package com.example.pi_week_2.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.pi_week_2.Photo;
import com.example.pi_week_2.PhotoSearchRecyclerView;
import com.example.pi_week_2.RecyclerViewHelper;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoadPhotosAsync extends AsyncTask<Void, Void, Void> {
    private ProgressBar pb;
    private List<Photo> photos;
    private PhotoSearchRecyclerView.PhotoAdapter adapter;
    private Context context;
    private RecyclerViewHelper helper;

    private int indexFrom;
    private int indexTo;

    public LoadPhotosAsync(Context context, RecyclerViewHelper helper, ProgressBar pb, List<Photo> photos, int indexFrom, int indexTo) {
        this.helper = helper;
        this.pb = pb;
        this.photos = photos;
        this.indexFrom = indexFrom;
        this.indexTo = indexTo;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            for (int i = indexFrom; i < indexTo; i++) {
                Log.i("DOWNLOADED LINK", photos.get(i).getLink());
                photos.get(i).setPhoto(Glide.with(context)
                        .asBitmap()
                        .load(photos.get(i).getLink())
                        .submit()
                        .get());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        pb.setVisibility(View.GONE);
        helper.getLoadedData(photos, indexFrom, indexTo);
    }
}