package com.example.pi_week_2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pi_week_2.Async.FindPhotosAsync;

public class MainActivity extends AppCompatActivity {
    private static final String RUNNING_TAG = "Downloading";
    private TextView searchText;
    private TextView photoLinks;
    private ProgressBar progressBar;

    private FindPhotosAsync fpa;
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

        fpa = (FindPhotosAsync) new FindPhotosAsync(searchText, photoLinks, progressBar).execute();
    }

    @Override
    public void startActivity(Intent intent) {
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_VIEW)) {
            Intent pIntent = new Intent(this, LookPhotoActivity.class);
            pIntent.putExtra("URL", intent.getData().toString());
            super.startActivity(pIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Lifecycle", "Stop");
        if (fpa != null)
            fpa.cancel(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (fpa != null) {
            outState.putString(RUNNING_TAG, "Is working");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getString(RUNNING_TAG) != null)
            searchPhotos(searchText);
    }
}