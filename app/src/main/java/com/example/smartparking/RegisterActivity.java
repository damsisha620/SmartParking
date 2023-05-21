package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullname,email,dob,mobile,aadhar,password,confirm;
    private ProgressBar progressBar;
    private RadioGroup gender;
    private RadioButton radiobuttongenderid;
    private DatePickerDialog picker;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
   // UserInfo user;
    private static final String TAG="RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        Toast.makeText(RegisterActivity.this, "You can register now", Toast.LENGTH_SHORT).show();

        fullname=findViewById(R.id.fullame_edit);
        email=findViewById(R.id.email_edit);
        dob=findViewById(R.id.dob_edit);
        mobile=findViewById(R.id.mobile_edit);
        aadhar=findViewById(R.id.aadhar_edit);
        password=findViewById(R.id.password_edit);
        confirm=findViewById(R.id.confirm_edit);
        gender=findViewById(R.id.gender_radio);
        gender.clearCheck();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        //user=new UserInfo();
        //Setting up DatePicker on Edittext
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);

                //Date picker dialog
                picker=new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                        dob.setText(dayofmonth+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                picker.show();
            }
        });
        Button reg_btn=findViewById(R.id.reg_button);


        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedgenderid=gender.getCheckedRadioButtonId();
                radiobuttongenderid=findViewById(selectedgenderid);
                String tfname=fullname.getText().toString();
                String temail=email.getText().toString();
                String tdob=dob.getText().toString();
                String tmobile=mobile.getText().toString();
                String taadhar=aadhar.getText().toString();
                String tpassword=password.getText().toString();
                String tconfirm=confirm.getText().toString();
                String tgender;
                String mobileRegex="[6-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern=Pattern.compile(mobileRegex);
                mobileMatcher=mobilePattern.matcher(tmobile);
                String aadharRegex="[2-9][0-9]{10}";
                Matcher aadharMatcher;
                Pattern aadharPattern=Pattern.compile(aadharRegex);
                aadharMatcher=aadharPattern.matcher(taadhar);
                progressBar=findViewById(R.id.progressbar);
                if(TextUtils.isEmpty(tfname))
                {
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    fullname.setError("Full name is required");
                    fullname.requestFocus();
                } else if (TextUtils.isEmpty(temail)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    email.setError("Email is required");
                    email.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(temail).matches())
                {
                    Toast.makeText(RegisterActivity.this, "Please enter valid email", Toast.LENGTH_LONG).show();
                    email.setError("Valid Email is required");
                    email.requestFocus();
                }

                else if (TextUtils.isEmpty(tdob)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
                    dob.setError("Date of birth is required");
                    dob.requestFocus();

                } else if (gender.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(RegisterActivity.this, "Select gender", Toast.LENGTH_LONG).show();
                    radiobuttongenderid.setError("Gender is required");
                    radiobuttongenderid.requestFocus();

                } else if (TextUtils.isEmpty(tmobile)) {
                    Toast.makeText(RegisterActivity.this, "Please enter mobile number", Toast.LENGTH_LONG).show();
                    mobile.setError("Mobile number is required");
                    mobile.requestFocus();

                }  else if (tmobile.length()!=10) {
                    Toast.makeText(RegisterActivity.this, "Re-enter mobile no", Toast.LENGTH_LONG).show();
                    mobile.setError("Mobile number should be 10 digits");
                    mobile.requestFocus();
                } else if (!mobileMatcher.find()) {
                    Toast.makeText(RegisterActivity.this, "Re-enter mobile no", Toast.LENGTH_LONG).show();
                    mobile.setError("Valid Mobile number is required");
                    mobile.requestFocus();
                }else if (TextUtils.isEmpty(taadhar)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your aadhar number", Toast.LENGTH_LONG).show();
                    aadhar.setError("Aadhar is required");
                    aadhar.requestFocus();

                } else if (taadhar.length()!=12) {
                    Toast.makeText(RegisterActivity.this, "Re-enter aadhar number", Toast.LENGTH_LONG).show();
                    aadhar.setError("Aadhar number is 12 digits");
                    aadhar.requestFocus();

                } else if (!aadharMatcher.find()) {
                    Toast.makeText(RegisterActivity.this, "Re-enter aadhar number", Toast.LENGTH_LONG).show();
                    aadhar.setError("Valid aadhar number is required");
                    aadhar.requestFocus();
                } else if (TextUtils.isEmpty(tpassword)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    password.setError("Password is required");
                    password.requestFocus();

                } else if (tpassword.length()<6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits", Toast.LENGTH_LONG).show();
                    password.setError("Password too weak");
                    password.requestFocus();

                } else if (TextUtils.isEmpty(tconfirm)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your confirm password", Toast.LENGTH_LONG).show();
                    confirm.setError("Confirm password is required");
                    confirm.requestFocus();

                } else if (!tpassword.equals(tconfirm)) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter password", Toast.LENGTH_LONG).show();
                    confirm.setError("Password confirmation is required");
                    confirm.requestFocus();
                    password.clearComposingText();
                    confirm.clearComposingText();

                }
                else {
                    tgender=radiobuttongenderid.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(tfname,temail,tdob,taadhar,tmobile,tgender,tpassword,tconfirm);
                }
            }
        });
    }

    private void registerUser(String tfname, String temail, String tdob, String taadhar, String tmobile, String tgender, String tpassword, String tconfirm) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(temail,tpassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this,"User registered Successfully",Toast.LENGTH_LONG).show();;
                    FirebaseUser firebaseUser=auth.getCurrentUser();

                    //Enter user data into the Firebase Realtime Database

                    UserInfo user=new UserInfo(tfname,temail,tdob,tgender,taadhar,tmobile);
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Registered User");
                    databaseReference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                //Send verification mail
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(RegisterActivity.this, "User registered Successfully", Toast.LENGTH_LONG).show();
                                //Open user profile after successful registration
                                Intent intent=new Intent(RegisterActivity.this,UserProfileActivity.class);
                                //To prevent user from returnback to Register Activity on pressing button after registration
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();//to close Register Activity
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                            }

                            //Hide ProgressBar whether User creation is successful or failed
                            progressBar.setVisibility(View.GONE);


                        }
                    });



                }else {
                    try {
                        {
                            throw task.getException();
                        }
                    }catch(FirebaseAuthWeakPasswordException e)
                    {
                        email.setError("Your password is too weak.kindly use a mix of alphabets,numbers and symbol");
                    }catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        email.setError("Your mail is invalid or already in use. Kindly re-enter.");
                    }catch (FirebaseAuthUserCollisionException e)
                    {
                        email.setError("User is already registered with this email.Use another email");
                    }catch(Exception e)
                    {
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

        });
    }
}