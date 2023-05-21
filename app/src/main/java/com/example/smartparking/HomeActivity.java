package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home");
        auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        CardView myhome=findViewById(R.id.cardMyAccount);
        CardView map=findViewById(R.id.cardMapView);
        CardView bookslot=findViewById(R.id.cardBookSlot);
        CardView previous=findViewById(R.id.cardParkingHistory);
        CardView feedback=findViewById(R.id.cardSendFeedback);
        CardView exit=findViewById(R.id.cardExit);
        Toast.makeText(HomeActivity.this, "Welcome to 24*7 Parking System", Toast.LENGTH_SHORT).show();


        myhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this,"User's Details",Toast.LENGTH_SHORT).show();
                //Start the HomeActivity
                startActivity(new Intent(HomeActivity.this,UserProfileActivity.class));
                finish();
            }
        });


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this,"Google Map",Toast.LENGTH_SHORT).show();
                //Start the HomeActivity
                startActivity(new Intent(HomeActivity.this,MapActivity.class));
                finish();
            }
        });

        bookslot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this,"Book slot",Toast.LENGTH_SHORT).show();
                //Start the HomeActivity
                startActivity(new Intent(HomeActivity.this,MapsActivity2.class));
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);

                //clear stack to prevent user coming back to Homeactivity on pressing back button after Logging out
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}