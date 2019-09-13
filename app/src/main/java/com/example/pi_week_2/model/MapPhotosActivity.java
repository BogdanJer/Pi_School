package com.example.pi_week_2.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

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
import com.example.pi_week_2.adapter.MapPhotosAdapter;
import com.example.pi_week_2.async.FindPhotosAsync;

import static com.example.pi_week_2.model.MainActivity.USER_TAG;

public class MapPhotosActivity extends AppCompatActivity {
    private MapPhotosAdapter adapter;
    private boolean gridView = true;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_photos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ProgressBar pb = findViewById(R.id.map_photos_progress);

        double latitude = getIntent().getDoubleExtra("Latitude", 30.0);
        double longitude = getIntent().getDoubleExtra("Longitude", 35.0);

        recyclerView = findViewById(R.id.map_photos_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new MapPhotosAdapter(this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, adapter));
        touchHelper.attachToRecyclerView(recyclerView);

        new FindPhotosAsync(pb, adapter, latitude, longitude).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.main_screen).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.history) {
            intent = new Intent(this, HistoryActivity.class);
            intent.putExtra(USER_TAG, MainActivity.name);
            startActivity(intent);
        } else if (id == R.id.change_view) {
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
        } else if (id == R.id.main_screen) {
            finish();
        }

        return true;
    }
}
