package com.example.pi_week_2;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.db.flickr.FlickrDAO;

import java.util.List;

import static com.example.pi_week_2.MainActivity.USER_TAG;

public class ShowFavoriteActivity extends AppCompatActivity {
    private String name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = getIntent().getStringExtra(USER_TAG);

        getSupportActionBar().setTitle(toolbar.getTitle() + "/" + name);

        FlickrDAO dao = FlickrDAO.getDao(this);
        List<Photo> list = dao.getUserFavorite(name);

        ProgressBar progressBar = findViewById(R.id.progress_bar_favorites);

        RecyclerView recyclerView = findViewById(R.id.show_favorites_recycler_view);

        PhotoFavoriteRecyclerView pfrv = new PhotoFavoriteRecyclerView(name, recyclerView, progressBar, list);
        pfrv.build(this);
    }
}