package com.example.pi_week_2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pi_week_2.Async.FindPhotosAsync;

public class MainActivity extends AppCompatActivity {

    private TextView searchText;
    private TextView photoLinks;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchText = findViewById(R.id.search_text);
        photoLinks = findViewById(R.id.photo_links);
        progressBar = findViewById(R.id.progress_bar);

        photoLinks.setMovementMethod(new ScrollingMovementMethod());
    }

    public void searchPhotos(View view) {
        LinearLayout searchLayout = findViewById(R.id.search_layout);

        searchLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        new FindPhotosAsync(searchText, photoLinks, progressBar).execute();
    }

    @Override
    public void startActivity(Intent intent) {
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_VIEW)) {
            Intent pIntent = new Intent(this, LookPhotoActivity.class);
            pIntent.putExtra("URL", intent.getData().toString());
            super.startActivity(pIntent);
        }
    }

}