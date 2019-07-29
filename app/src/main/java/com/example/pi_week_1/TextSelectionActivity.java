package com.example.pi_week_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TextSelectionActivity extends AppCompatActivity {
    public static final String REPLY_TEXT = "REPLY TEXT";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_selection);
    }

    public void onAnswerChosen(View view) {
        returnResult(((TextView) view).getText().toString());
    }
    public void returnResult(String text){
        Intent intent = new Intent();

        intent.putExtra(REPLY_TEXT,text);
        setResult(MainActivity.ANOTHER_ACTIVITY,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        returnResult("");
    }
}
