package com.example.pi_week_2;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.pi_week_2.db.flickr.FlickrDAO;

import static com.example.pi_week_2.MainActivity.USER_TAG;

public class ShowFavoriteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String name = getIntent().getStringExtra(USER_TAG);

        getSupportActionBar().setTitle(toolbar.getTitle() + "/" + name);

        TextView favoriteText = findViewById(R.id.show_favorite_text);
        favoriteText.setMovementMethod(new ScrollingMovementMethod());

        FlickrDAO dao = FlickrDAO.getDao(this);
        favoriteText.setText(dao.getUserFavorite(name));

        Linkify.addLinks(favoriteText, Linkify.WEB_URLS);
    }
}