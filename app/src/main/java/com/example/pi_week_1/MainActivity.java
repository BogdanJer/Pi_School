package com.example.pi_week_1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int ANOTHER_ACTIVITY = 8;
    private int count = 0;
    private boolean isAnotherActivity = false;
    private TextView incomingText;
    private TextView shareText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incomingText = findViewById(R.id.incoming_text);
        shareText = findViewById(R.id.share_text);

        Intent intent = getIntent();

        String text = intent.getStringExtra(Intent.EXTRA_TEXT);

        if(text!=null) {
            incomingText = findViewById(R.id.incoming_text);
            incomingText.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }

    public void chooseText(View view){
        Intent intent = new Intent(this,TextSelectionActivity.class);
        startActivityForResult(intent,ANOTHER_ACTIVITY);
    }
    @Override
    protected void onStart(){
        super.onStart();

        if(!isAnotherActivity) {
            TextView counter = findViewById(R.id.counter);
            counter.setText(String.format("%x times", count++));
        }
    }
    @Override
    protected void onResume(){
        super.onResume();


    }
    @Override
    protected void onStop() {
        super.onStop();
        isAnotherActivity = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==ANOTHER_ACTIVITY){
                isAnotherActivity = true;
                incomingText = findViewById(R.id.share_text);
                incomingText.setText(data.getStringExtra(TextSelectionActivity.REPLY_TEXT));
        }
    }
    public void shareText(View view){
        String txt = ((TextView)findViewById(R.id.share_text)).getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,txt);

        startActivity(Intent.createChooser(intent, getResources().getText(R.string.chooser_title)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("Count",count);
        outState.putBoolean("Another_activity",isAnotherActivity);
        outState.putString("Incoming_text",incomingText.getText().toString());
        outState.putString("Share_text",shareText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        count = savedInstanceState.getInt("Count");
        isAnotherActivity = savedInstanceState.getBoolean("Another_activity");
        shareText.setText(savedInstanceState.getString("Share_text"));
        incomingText.setText(savedInstanceState.getString("Incoming_text"));

    }
}