package com.example.pi_week_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.adapter.FavoriteAdapter;
import com.example.pi_week_2.db.flickr.FlickrDAO;
import com.example.pi_week_2.holder.FavoriteHolder;

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

        ItemTouchHelper.Callback callback = new FavoriteTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private class FavoriteTouchHelper extends ItemTouchHelper.SimpleCallback {
        public FavoriteTouchHelper(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.removeItem((FavoriteHolder) viewHolder);
        }
    }
}