package com.example.pi_week_2.model;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.pi_week_2.R;
import com.example.pi_week_2.db.flickr.FlickrDAO;
import com.example.pi_week_2.storage.ExternalStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class LookPhotoActivity extends AppCompatActivity {
    private final int WRITE_TO_EXTERNAL_STORAGE_REQUEST = 1;
    private String url;
    private String user;
    private String searchWord;

    private FlickrDAO dao = FlickrDAO.getDao(this);
    private ExternalStorage storage;

    private ImageButton favorite;
    private ImageButton downloadBut;

    private boolean isDownloaded = false;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_photo);

        url = getIntent().getStringExtra(MainActivity.URL_TAG);
        user = getIntent().getStringExtra(MainActivity.USER_TAG);
        searchWord = getIntent().getStringExtra(MainActivity.SEARCH_TAG);

        storage = new ExternalStorage(this);

        TextView searchWordView = findViewById(R.id.search_word_view);
        searchWordView.setText(searchWord);

        WebView web = findViewById(R.id.web_page);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        web.loadUrl(url);

        favorite = findViewById(R.id.favorite_but);
        downloadBut = findViewById(R.id.download_photo);

        if (dao.userHasPhoto(user, url)) {
            favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
            isFavorite = true;
        }
        if (dao.getSavedPhoto(user, url) != -1) {
            downloadBut.setImageDrawable(getResources().getDrawable(R.drawable.done));
            isDownloaded = true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "App needs to write and read a device storage", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_TO_EXTERNAL_STORAGE_REQUEST);
                }
            }
        }
    }

    public void addToFavorite(View view) {
        ImageButton favorite = findViewById(R.id.favorite_but);

        if (isFavorite) {
            favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
            dao.deletePhoto(user, url);
            Toast.makeText(this, R.string.delete_from_favorite, Toast.LENGTH_LONG).show();
            isFavorite = false;
            return;
        }

        dao.insertPhoto(user, searchWord, url);

        Toast.makeText(this, R.string.photo_is_added, Toast.LENGTH_LONG).show();
        isFavorite = true;
        favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
    }

    public void savePhoto(View view) {
        if (isDownloaded) {
            dao.deleteSavedPhoto(user, url);
            downloadBut.setImageDrawable(getResources().getDrawable(R.drawable.download));
            isDownloaded = false;
            return;
        }

        isDownloaded = true;
        new DownloadPictureAsync(url, this).execute();
    }

    public void getDownloadedPhoto(File file) {
        byte[] arr = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            arr = new byte[fis.available()];

            fis.read(arr);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        storage.savePhoto(BitmapFactory.decodeByteArray(arr, 0, arr.length), url);

        downloadBut.setImageDrawable(getResources().getDrawable(R.drawable.done));
    }

    private class DownloadPictureAsync extends AsyncTask<Void, Void, File> {
        private String url;
        private LookPhotoActivity activity;

        public DownloadPictureAsync(String url, LookPhotoActivity activity) {
            this.url = url;
            this.activity = activity;
        }

        @Override
        protected File doInBackground(Void... voids) {
            File file = null;
            try {
                file = Glide.with(activity)
                        .downloadOnly()
                        .load(url)
                        .submit()
                        .get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            activity.getDownloadedPhoto(file);
        }
    }
}
