package com.example.smartparking;

import static com.example.smartparking.R.id.progressbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
private TextView textViewfullname,textviewwelcome,textviewdob,textviewgender,textviewmobile,textviewemail;
private ProgressBar progressBar;
private String fullname,email,dob,gender,mobile;
private ImageView imageView;
private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile2);
        getSupportActionBar().setTitle("Home");

        textviewwelcome=findViewById(R.id.text1);
        textViewfullname=findViewById(R.id.textname);
        textviewemail=findViewById(R.id.textemail);
        textviewdob=findViewById(R.id.textdob);
        textviewgender=findViewById(R.id.textgender);
        textviewmobile=findViewById(R.id.textphone);
        progressBar=findViewById(R.id.progressbar);

        //Set onclickListener on ImageView to open UploadProfile picture
        imageView=findViewById(R.id.profile_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserProfileActivity.this,UploadProfileActivity.class);
                startActivity(intent);
            }
        });

        auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();

        if(firebaseUser==null)
        {
            Toast.makeText(UserProfileActivity.this, "Something went wrong and user's details are not found", Toast.LENGTH_LONG).show();

        }else {
            checkifEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);

            showUserProfile(firebaseUser);
        }
    }

    private void checkifEmailVerified(FirebaseUser firebaseUser) {


        if(!firebaseUser.isEmailVerified()) {
            firebaseUser.sendEmailVerification();
            auth.signOut();//signout user
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        //Set up the alert builder
        AlertDialog.Builder builder=new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Email is not verified");
        builder.setMessage("Please verify your email now. You can not login without email verification next time.");

        //open Email app
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//To email app in new window and not within new app
                startActivity(intent);
            }
        });

        //Create alert Dialog box
        AlertDialog alertDialog=builder.create();
        //show the alertdialog box
        alertDialog.show();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
      String userID=firebaseUser.getUid();

      //Extracting user Reference from Database for "Registered user"
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Registered User");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails user=snapshot.getValue(ReadWriteUserDetails.class);
                if(user!=null)
                {
                    fullname=user.fullname;
                    email=user.email;
                    dob=user.dob;
                    gender=user.gender;
                    mobile=user.mobile;

                    textviewwelcome.setText("Welcome "+fullname+"!");
                    textViewfullname.setText(fullname);
                    textviewemail.setText(email);
                    textviewdob.setText(dob);
                    textviewgender.setText(gender);
                    textviewmobile.setText(mobile);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    //create Action Bar

    @Override
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
        } else if (id==R.id.home) {
            Intent intent=new Intent(UserProfileActivity.this,HomeActivity.class);
            startActivity(intent);

        } else if (id==R.id.change_pass) {
            Intent intent=new Intent(UserProfileActivity.this,ChangePasswordActivity.class);
            startActivity(intent);

        } else if (id==R.id.delete_profile) {
            Intent intent=new Intent(UserProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);

        } else if (id==R.id.menu_logout) {
            auth.signOut();
            Toast.makeText(UserProfileActivity.this, "Logged out", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(UserProfileActivity.this,MainActivity.class);

            //clear stack to prevent user coming back to Userprofileactivity on pressing back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }
        else {
            Toast.makeText(UserProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}