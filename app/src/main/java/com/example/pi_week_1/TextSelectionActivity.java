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
    @Override
    protected void onStop(){
        super.onStop();

    }
    public void firstAnswerIsChosen(View view){
        returnResult(((TextView)findViewById(R.id.text_to_choose1)).getText().toString());
    }
    public void secondAnswerIsChosen(View view){
        returnResult(((TextView)findViewById(R.id.text_to_choose2)).getText().toString());
    }
    public void thirdAnswerIsChosen(View view){
        returnResult(((TextView)findViewById(R.id.text_to_choose3)).getText().toString());
    }
    public void fourthAnswerIsChosen(View view){
        returnResult(((TextView)findViewById(R.id.text_to_choose4)).getText().toString());
    }
    public void fifthAnswerIsChosen(View view){
        returnResult(((TextView)findViewById(R.id.text_to_choose5)).getText().toString());
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
