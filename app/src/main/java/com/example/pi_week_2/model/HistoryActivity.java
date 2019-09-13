package com.example.pi_week_2.model;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.R;
import com.example.pi_week_2.adapter.SimpleStringAdapter;
import com.example.pi_week_2.db.flickr.FlickrDAO;

import java.util.List;

import static com.example.pi_week_2.model.MainActivity.USER_TAG;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String userName = getIntent().getStringExtra(USER_TAG);

        getSupportActionBar().setTitle(toolbar.getTitle() + "/" + userName);

        List<String> list = FlickrDAO.getDao(this).getUserHistory(userName);


        RecyclerView recyclerView = findViewById(R.id.history_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new SimpleStringAdapter(list));
    }
}
