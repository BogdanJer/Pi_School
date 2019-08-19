package com.example.pi_week_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.async.FindPhotosAsync;
import com.example.pi_week_2.db.flickr.FlickrDAO;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {
    public static final String SEARCH_TAG = "Search word";
    public static final String RUNNING_TAG = "Downloading";
    public static final String USER_TAG = "User";
    public static final String URL_TAG = "Url";

    private EditText searchText;
    private TextView photoLinks;
    private ProgressBar progressBar;
    private LinearLayout searchLayout;
    private RecyclerView recyclerView;

    private FindPhotosAsync fpa;

    private PhotoSearchRecyclerView psrv;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchText = findViewById(R.id.search_text);
        //photoLinks = findViewById(R.id.photo_links);
        progressBar = findViewById(R.id.progress_bar);

        searchLayout = findViewById(R.id.search_layout);

        Stetho.initializeWithDefaults(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = getIntent().getStringExtra(USER_TAG);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);

        searchText.setText(preferences.getString(SEARCH_TAG, ""));

        recyclerView = findViewById(R.id.photos_recycler_view);
    }

    public void searchPhotos(View view) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
            return;
        }

        String searchWord = searchText.getText().toString();

        searchLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        FlickrDAO.getDao(this).insertHistoryNote(name, searchWord);

        psrv = new PhotoSearchRecyclerView(recyclerView, name, searchWord);

        if (PhotoSearchRecyclerView.isBuild) {
            PhotoSearchRecyclerView psrv = new PhotoSearchRecyclerView(recyclerView, name, searchWord);
            psrv.build(this, progressBar);
        } else
            PhotoSearchRecyclerView.photoSearchRecyclerView.reBuild(searchWord);
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
        if (id == R.id.action_settings) {
            intent = new Intent(this, ShowFavoriteActivity.class);
            intent.putExtra(USER_TAG, name);

            startActivity(intent);
        } else if (id == R.id.history) {
            intent = new Intent(this, HistoryActivity.class);
            intent.putExtra(USER_TAG, name);

            startActivity(intent);
        } else if (id == R.id.change_view) {
            //((ActionMenuItemView)findViewById(id)).setIcon(getResources().getDrawable(R.drawable.change_view));
            // psrv.reBuild(new LinearLayoutManager(this));
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
}