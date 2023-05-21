package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GoogleMapActivity2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map2);
        getSupportActionBar().setTitle("Map View");
    }
}