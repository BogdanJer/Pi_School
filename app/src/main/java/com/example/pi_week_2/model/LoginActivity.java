package com.example.pi_week_2.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pi_week_2.R;
import com.example.pi_week_2.db.flickr.FlickrDAO;

import static com.example.pi_week_2.model.MainActivity.USER_TAG;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
    }

    public void login(View view) {
        EditText loginText = findViewById(R.id.login_text);
        String name = loginText.getText().toString();

        if (name == null) {
            Toast.makeText(this, R.string.login_required, Toast.LENGTH_LONG).show();
            return;
        }
        FlickrDAO dao = FlickrDAO.getDao(this);


        if (dao.getUserId(name) == -1) {
            dao.insertUser(name);

            Toast.makeText(getBaseContext(), R.string.user_is_added, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.user_exist, Toast.LENGTH_LONG).show();
        }


        FlickrDAO.getDbHelper().close();

        Handler h = new Handler();
        h.postDelayed(() -> {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra(USER_TAG, name);
            startActivity(intent);
        }, 1500);
    }
}
