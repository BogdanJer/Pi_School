package com.example.pi_week_2.model;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.Photo;
import com.example.pi_week_2.R;
import com.example.pi_week_2.RecyclerItemTouchHelper;
import com.example.pi_week_2.adapter.FavoriteAdapter;
import com.example.pi_week_2.db.flickr.FlickrDAO;

import java.util.List;

public class ShowFavoriteActivity extends AppCompatActivity {
    private String name;
    private FavoriteAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(toolbar.getTitle() + "/" + MainActivity.name);

        FlickrDAO dao = FlickrDAO.getDao(this);
        List<Photo> list = dao.getUserFavorite(MainActivity.name);

        RecyclerView recyclerView = findViewById(R.id.show_favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoriteAdapter(this, list);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}