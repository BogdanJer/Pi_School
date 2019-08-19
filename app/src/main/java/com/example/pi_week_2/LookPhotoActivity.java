package com.example.pi_week_2;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pi_week_2.db.flickr.FlickrDAO;

import static com.example.pi_week_2.MainActivity.SEARCH_TAG;
import static com.example.pi_week_2.MainActivity.URL_TAG;
import static com.example.pi_week_2.MainActivity.USER_TAG;

public class LookPhotoActivity extends AppCompatActivity {
    private String url;
    private String user;
    private String searchWord;

    private FlickrDAO dao = FlickrDAO.getDao(this);

    private ImageButton favorite;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_photo);

        url = getIntent().getStringExtra(URL_TAG);
        user = getIntent().getStringExtra(USER_TAG);
        searchWord = getIntent().getStringExtra(SEARCH_TAG);

        TextView searchWordView = findViewById(R.id.search_word_view);
        searchWordView.setText(searchWord);

        WebView web = findViewById(R.id.web_page);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        web.loadUrl(url);

        favorite = findViewById(R.id.favorite_but);

        if (dao.userHasPhoto(user, url))
            favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
    }

    public void addToFavorite(View view) {
        ImageButton favorite = findViewById(R.id.favorite_but);

        if (dao.userHasPhoto(user, url)) {
             favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
            dao.deletePhoto(user, url);
            return;
        }

        dao.insertPhoto(user, searchWord, url);

        Toast.makeText(this, R.string.photo_is_added, Toast.LENGTH_LONG).show();

        favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
    }
}
