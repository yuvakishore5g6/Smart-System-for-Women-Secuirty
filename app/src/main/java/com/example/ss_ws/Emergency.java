package com.example.ss_ws;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Emergency extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        getSupportActionBar().setTitle("Emergency contacts");
    }
}
