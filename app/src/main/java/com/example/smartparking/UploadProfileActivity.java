package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

public class UploadProfileActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageView imageView;
    private FirebaseAuth auth;
    private FirebaseStorage storageReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);
        getSupportActionBar().setTitle("Upload profile picture");
        auth=FirebaseAuth.getInstance();
        Button btnchose_pic=findViewById(R.id.chose_img);
        Button btnupload_pic=findViewById(R.id.upload_picbtn);
        imageView=findViewById(R.id.image_profile);
        progressBar=findViewById(R.id.progressbar);

        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();

        storageReference= FirebaseStorage.getInstance("Display pics");

        Uri uri=firebaseUser.getPhotoUrl();

        //Set user's current DP in Imageview(if uploaded already).We will Picasso since imageviewer setImage
        //Regular URI's
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.menu_refresh)
        {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }/* else if (id==R.id.menu_settings) {
            Toast.makeText(UserProfileActivity.this, "Menu Settings", Toast.LENGTH_SHORT).show();

        } else if (id==R.id.change_pass) {
            Intent intent=new Intent(UserProfileActivity.this,ChangePasswordActivity.class);
            startActivity(intent);

        } else if (id==R.id.delete_profile) {
            Intent intent=new Intent(UserProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);

        } else if (id==R.id.menu_logout) {
            auth.signOut();
            Toast.makeText(UploadProfileActivity.this, "Logged out", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(UploadProfileActivity.this,MainActivity.class);

            //clear stack to prevent user coming back to Uploadprofileactivity on pressing back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }
        else {
            Toast.makeText(UploadProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);*/
    }
}