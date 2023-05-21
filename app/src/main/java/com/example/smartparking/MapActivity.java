package com.example.smartparking;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MapActivity extends AppCompatActivity {

    //Initialize variable
    EditText esource,edestination;
    Button btn_track;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        esource=findViewById(R.id.source);

        //Assign Variable
        edestination=findViewById(R.id.destination);
        btn_track=findViewById(R.id.track);

        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get value from edit text

                String source=esource.getText().toString().trim();
                String destination=edestination.getText().toString().trim();

                //Check condition
                if(TextUtils.isEmpty(source) && TextUtils.isEmpty(destination))
                {
                    Toast.makeText(MapActivity.this, "Enter both location", Toast.LENGTH_SHORT).show();
                }else {
                    //When both value fill
                    DisplayTrack(source,destination);
                }
            }
        });

    }

    private void DisplayTrack(String source, String destination) {

        //If the device does not have a map installed, then redirect it to play store
        try{
            //when google map is installed
            //Installed URI
            Uri uri=Uri.parse("https://www.google.co.in/maps/dir/"+source+"/"+destination);

            //Initialize intent with action view
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            //Set Package
            intent.setPackage("com.google.android.apps.maps");
            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Start Activity
            startActivity(intent);
        }catch (ActivityNotFoundException e)
        {
            //When google map is not installed

            //Installation uri
            Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");

            //Initialize intent with action view
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            //Set Flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //start activity
            startActivity(intent);
        }
    }
}