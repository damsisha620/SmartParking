package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    // UserInfo user;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        Toast.makeText(LoginActivity.this, "You can login now", Toast.LENGTH_SHORT).show();
            email=findViewById(R.id.email_editl);
            password=findViewById(R.id.password_editl);
            progressBar=findViewById(R.id.progressbar1);

            firebaseAuth=FirebaseAuth.getInstance();
            //Show and Hide password
        ImageView imageView=findViewById(R.id.show_hide_password);
        imageView.setImageResource(R.drawable.icon_hide_pwd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    //If password is visible then hide it
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    imageView.setImageResource(R.drawable.icon_hide_pwd);
                }
                else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageView.setImageResource(R.drawable.icon_show_pwd);
                }
            }
        });
        TextView tv1=findViewById(R.id.text_forgot);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "You can reset your password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,ForgotpasswordActivity.class));
            }
        });


        //Login user
        Button btn=findViewById(R.id.login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temail=email.getText().toString();
                String tpassword=password.getText().toString();
                if(TextUtils.isEmpty(temail))
                {
                    Toast.makeText(LoginActivity.this, "Please fill email address", Toast.LENGTH_SHORT).show();
                    email.setError("Email is required");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(temail).matches()) {
                    Toast.makeText(LoginActivity.this, "Re-enter email address", Toast.LENGTH_SHORT).show();
                    email.setError("Valid Email is required");
                    email.requestFocus();

                } else if (TextUtils.isEmpty(tpassword)) {
                    Toast.makeText(LoginActivity.this, "Please fill password", Toast.LENGTH_SHORT).show();
                    password.setError("Password is required");
                    password.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginuser(temail,tpassword);
                }
            }
        });


        }

    private void loginuser(String textemail, String textpassword) {
        firebaseAuth.signInWithEmailAndPassword(textemail,textpassword).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //Get Instance of current user
                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                    if(firebaseUser.isEmailVerified())
                    {
                        Toast.makeText(LoginActivity.this,"You are Logged in now",Toast.LENGTH_SHORT).show();
                        //Start the HomeActivity
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();

                    }
                    else {
                        firebaseUser.sendEmailVerification();
                        firebaseAuth.signOut();//signout user
                        showAlertDialog();
                    }
                }else {
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthInvalidUserException e)
                    {
                        email.setError("User doesn't exist please register again");
                        email.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e){
                       email.setError("Invalid Credentials.Kindly check and re-enter");
                       email.requestFocus();

                    }
                    catch(Exception e)
                    {
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        //Set up the alert builder
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email is not verified");
        builder.setMessage("Please verify your email address");

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
//Checked if user is already logged in. In such case, straightaway take the user to User's profile
    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            Toast.makeText(LoginActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();
            //Start the UserprofileActivity
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }
        else {
            Toast.makeText(LoginActivity.this, "You can login now", Toast.LENGTH_SHORT).show();
        }
    }
}
