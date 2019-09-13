package com.example.pi_week_2.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.R;
import com.example.pi_week_2.RecyclerItemTouchHelper;
import com.example.pi_week_2.adapter.PhotoAdapter;
import com.example.pi_week_2.async.FindPhotosAsync;
import com.example.pi_week_2.db.flickr.FlickrDAO;
import com.example.pi_week_2.storage.InternalStorage;
import com.facebook.stetho.Stetho;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final String SEARCH_TAG = "Search word";
    public static final String RUNNING_TAG = "Downloading";
    public static final String USER_TAG = "User";
    public static final String URL_TAG = "Url";

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText searchText;
    private TextView photoLinks;
    private ProgressBar progressBar;
    private LinearLayout searchLayout;
    private RecyclerView recyclerView;

    private BottomNavigationView navigationView;

    private InternalStorage storage;

    public static String name;
    private PhotoAdapter adapter;
    private FindPhotosAsync fpa;
    private RecyclerView.LayoutManager manager;
    private boolean gridView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchText = findViewById(R.id.search_text);
        progressBar = findViewById(R.id.progress_bar);

        searchLayout = findViewById(R.id.search_layout);

        Stetho.initializeWithDefaults(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = getIntent().getStringExtra(USER_TAG);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);

        searchText.setText(preferences.getString(SEARCH_TAG, ""));

        storage = new InternalStorage(this);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Intent intent = null;
            switch (menuItem.getItemId()) {
                case R.id.favorite_bottom_nav:
                    intent = new Intent(getBaseContext(), ShowFavoriteActivity.class);
                    break;
                case R.id.map_bottom_nav:
                    intent = new Intent(getBaseContext(), MapsActivity.class);
                    break;
                case R.id.gallery_nav:
                    intent = new Intent(getBaseContext(), GalleryActivity.class);
            }

            if (intent != null)
                startActivity(intent);
            return true;
        });

        recyclerView = findViewById(R.id.photos_recycler_view);
        adapter = new PhotoAdapter(this);
        manager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void searchPhotos(View view) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
            return;
        }

        String searchWord = searchText.getText().toString();

        searchLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        adapter.setTag(searchWord);

        FlickrDAO.getDao(this).insertHistoryNote(name, searchWord);
        fpa = (FindPhotosAsync) new FindPhotosAsync(progressBar, adapter, searchWord).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;

        switch (id) {
            case R.id.history:
                intent = new Intent(this, HistoryActivity.class);
                intent.putExtra(USER_TAG, name);
                startActivity(intent);
                break;
            case R.id.change_view:
                ActionMenuItemView menuItemView = findViewById(id);

                if (gridView) {
                    menuItemView.setIcon(getResources().getDrawable(R.drawable.linear_view_light));
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    gridView = false;
                } else {
                    menuItemView.setIcon(getResources().getDrawable(R.drawable.grid_view_light));
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                    gridView = true;
                }
        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fpa != null)
            fpa.cancel(true);

        getPreferences(Context.MODE_PRIVATE).edit().putString(SEARCH_TAG, searchText.getText().toString()).apply();
    }
    @Override
    protected void onStart() {
        super.onStart();
        navigationView.getMenu().findItem(R.id.search_bottom_nav).setChecked(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (fpa != null) {
            outState.putString(RUNNING_TAG, "Is working");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getString(RUNNING_TAG) != null)
            searchPhotos(searchText);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void takePicture(View view) {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "Doesn't available on this device!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            String fileName = storage.savePhoto(photo, "");

            startUcrop(Uri.fromFile(new File(fileName)));

        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri imageUri = UCrop.getOutput(data);

            if (imageUri != null)
                Toast.makeText(this, "Photo is saved", Toast.LENGTH_LONG).show();
        }
    }

    private void startUcrop(Uri uri) {
        UCrop uCrop = UCrop.of(uri, uri);

        uCrop.withAspectRatio(16, 9);

        uCrop.withMaxResultSize(1920, 1080);
        uCrop.withOptions(getUcropOptions());

        uCrop.start(MainActivity.this);
    }

    private UCrop.Options getUcropOptions() {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionQuality(0);

        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        //options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        return options;
    }
}