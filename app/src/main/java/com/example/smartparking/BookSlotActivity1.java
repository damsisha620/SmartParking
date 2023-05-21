package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookSlotActivity1 extends AppCompatActivity implements LocationListener {
    private EditText phone_txt, current_txt, destination_txt, vehicle_txt, vehicle_no, date_txt, duration_txt;
    private Button Book;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private DatePickerDialog picker;
    ImageButton imageButton;
    LocationManager locationManager;
    private FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    ProgressBar progressBar;
    private TextView markerTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_slot1);
        getSupportActionBar().setTitle("Book Slot");
        markerTxt=findViewById(R.id.marker);
        String title=getIntent().getStringExtra("title");
        markerTxt.setText(title);
        phone_txt = findViewById(R.id.phone_editl);
        destination_txt = findViewById(R.id.destination_edit);
        vehicle_txt = findViewById(R.id.vehicle_edit);
        vehicle_no = findViewById(R.id.vehicleno_edit);
        date_txt = findViewById(R.id.date_edit);
        duration_txt = findViewById(R.id.duration_edit);
        Book = findViewById(R.id.book);


        //Get Current Location
        //Runtime permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(BookSlotActivity1.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        imageButton = findViewById(R.id.icon_location);
        current_txt = findViewById(R.id.edit_location);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create method
                getLocation();

            }
        });


        //Setting up DatePicker on Edittext
        date_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date picker dialog
                picker = new DatePickerDialog(BookSlotActivity1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                        date_txt.setText(dayofmonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Book.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String phone = phone_txt.getText().toString();
                String current = current_txt.getText().toString();
                String destination = destination_txt.getText().toString();
                String vechiletype = vehicle_txt.getText().toString();
                String vechileno = vehicle_no.getText().toString();
                String date = date_txt.getText().toString();
                String duration = duration_txt.getText().toString();
                progressBar = findViewById(R.id.progressbar1);

                String mobileRegex = "[6-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(phone);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("Booking");
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(BookSlotActivity1.this, "Please enter your phone number", Toast.LENGTH_LONG).show();
                    phone_txt.setError("Phone number is required");
                    phone_txt.requestFocus();
                } else if (phone.length() != 10) {
                    Toast.makeText(BookSlotActivity1.this, "Re-enter mobile no", Toast.LENGTH_LONG).show();
                    phone_txt.setError("Mobile number should be 10 digits");
                    phone_txt.requestFocus();
                } else if (!mobileMatcher.find()) {
                    Toast.makeText(BookSlotActivity1.this, "Re-enter mobile no", Toast.LENGTH_LONG).show();
                    phone_txt.setError("Valid Mobile number is required");
                    phone_txt.requestFocus();
                } else if (TextUtils.isEmpty(current)) {
                    Toast.makeText(BookSlotActivity1.this, "Please click location icon", Toast.LENGTH_LONG).show();
                    current_txt.setError("Current location is required");
                    current_txt.requestFocus();
                } else if (TextUtils.isEmpty(destination)) {
                    Toast.makeText(BookSlotActivity1.this, "Please enter ddestination", Toast.LENGTH_LONG).show();
                    destination_txt.setError("Destination is required");
                    destination_txt.requestFocus();

                } else if (TextUtils.isEmpty(vechiletype)) {
                    Toast.makeText(BookSlotActivity1.this, "Please enter vechicle type", Toast.LENGTH_LONG).show();
                    vehicle_txt.setError("Vechicle type is required");
                    vehicle_txt.requestFocus();

                } else if (TextUtils.isEmpty(vechileno)) {
                    Toast.makeText(BookSlotActivity1.this, "Please enter vechicle number", Toast.LENGTH_LONG).show();
                    vehicle_no.setError("Vechicle number is required");
                    vehicle_no.requestFocus();
                } else if (TextUtils.isEmpty(duration)) {
                    Toast.makeText(BookSlotActivity1.this, "Please enter duration", Toast.LENGTH_LONG).show();
                    duration_txt.setError("Duration is required");
                    duration_txt.requestFocus();

                } else {
                    Book(phone, current, destination, vechiletype, vechileno, date, duration);
                }


            }
        });
    }

    private void Book(String phone, String current, String destination, String vechiletype, String vechileno, String date, String duration) {
        Bookdetails bookdetails = new Bookdetails(phone, current, destination, vechiletype, vechileno, date, duration);
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(bookdetails);

        Intent intent = new Intent(BookSlotActivity1.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(BookSlotActivity1.this, "Booking Successful", Toast.LENGTH_SHORT).show();
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, BookSlotActivity1.this);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, ""+location.getLatitude()+""+location.getLongitude(), Toast.LENGTH_SHORT).show();

        try{
            Geocoder geocoder=new Geocoder(BookSlotActivity1.this, Locale.getDefault());
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address=addresses.get(0).getAddressLine(0);
            current_txt.setText(address);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
