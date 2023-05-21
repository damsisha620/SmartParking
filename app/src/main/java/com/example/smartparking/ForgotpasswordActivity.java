package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class ForgotpasswordActivity extends AppCompatActivity {
private Button btnfor_pass;
private EditText emailp;
private ProgressBar progressBar;
private FirebaseAuth auth;
private final static String TAG="ForgotpasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        getSupportActionBar().setTitle("Forgot Password");
        emailp=findViewById(R.id.editEmail);
        btnfor_pass=findViewById(R.id.forget_pass);
        progressBar=findViewById(R.id.progressbar3);
        btnfor_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailp.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(ForgotpasswordActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                    emailp.setError("Email is required");
                    emailp.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ForgotpasswordActivity.this, "Please enter your valid registered email", Toast.LENGTH_SHORT).show();
                    emailp.setError("Valid email is required");
                    emailp.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    resetPassword(email);
                }
            }
        });
    }

    private void resetPassword(String email) {
        auth=FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgotpasswordActivity.this, "Please check your inbox for password reset link", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ForgotpasswordActivity.this,MainActivity.class);

                    //clear stack to prevent user coming back to ForgotpasswordActivity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        emailp.setError("User doesn't exist or is no longer valid.Please register again");
                    }catch(Exception e)
                    {
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(ForgotpasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}