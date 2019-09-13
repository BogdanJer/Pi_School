package com.example.pi_week_2.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.example.pi_week_2.adapter.GalleryAdapter;
import com.example.pi_week_2.db.flickr.Image;
import com.example.pi_week_2.storage.ExternalStorage;
import com.example.pi_week_2.storage.InternalStorage;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    private GalleryAdapter adapter;
    private RecyclerView recyclerView;

    private boolean gridView = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Gallery/" + MainActivity.name);

        recyclerView = findViewById(R.id.gallery_recycler_view);

        InternalStorage storage = new InternalStorage(this);
        ExternalStorage externalStorage = new ExternalStorage(this);

        List<Image> list = new ArrayList<>();

        if (storage.getPhotos(MainActivity.name) != null)
            list.addAll(storage.getPhotos(MainActivity.name));

        if (externalStorage.getPhotos(MainActivity.name) != null)
            list.addAll(externalStorage.getPhotos(MainActivity.name));

        if (list.size() == 0) {
            findViewById(R.id.no_photo_layout).setVisibility(View.VISIBLE);
            return;
        }
        sortImageByDate(list);

        adapter = new GalleryAdapter(list, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, adapter));
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.history:
                intent = new Intent(this, HistoryActivity.class);
                intent.putExtra(MainActivity.USER_TAG, MainActivity.name);
                startActivity(intent);
                break;
            case R.id.change_view:
                ActionMenuItemView menuItemView = findViewById(item.getItemId());

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

    private void sortImageByDate(List<Image> list) {
        int max;
        for (int i = 0; i < list.size() - 1; i++) {
            max = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(max).getTimeOfModified() < list.get(j).getTimeOfModified()) {
                    max = j;
                }
            }

            Image tempPhoto = list.get(i);
            list.set(i, list.get(max));
            list.set(max, tempPhoto);
        }
    }
}
