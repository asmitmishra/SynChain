package com.example.mishr.logme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class FirstLauncher extends AppCompatActivity {
    private static int TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launcher);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(FirstLauncher.this, Main2Activity.class);
                startActivity(homeIntent);
                finish();
            }
        },TIME_OUT);
    }
}
