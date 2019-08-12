package com.example.pi_week_2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.pi_week_2.db.flickr.FlickrDAO;
import com.example.pi_week_2.db.flickr.pojo.HistoryNote;

import java.util.List;

import static com.example.pi_week_2.MainActivity.USER_TAG;

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

        TextView historyView = findViewById(R.id.history_view);

        FlickrDAO dao = FlickrDAO.getDao(this);

        List<HistoryNote> list = dao.getUserHistory(userName);

        int len = list.size() > 20 ? 20 : list.size();

        StringBuffer text = new StringBuffer();
        for (int i = list.size() - 1, j = 1; i >= list.size() - len; i--)
            text.append(j++ + ": " + list.get(i).toString());

        historyView.setText(text.toString());
    }
}
