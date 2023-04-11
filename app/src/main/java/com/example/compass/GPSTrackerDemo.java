package com.example.compass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.Priority;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

public class GPSTrackerDemo extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    public static final int PERMISSIONS_FINE_LOCATION = 99;
    TextView tv_lat, tv_log, tv_altitude, tv_accuracy, tv_sensor, tv_speed, tv_address, tv_update;
    Switch sw_locationUpdates, sw_gps;

    boolean updateOn = true;

    //location request is a config file for all setting related to FusedLocationProviderClient
    LocationRequest locationRequest;

    //Google API for location service.
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpstracker_demo);

        tv_lat = findViewById(R.id.tv_lat);
        tv_log = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_address = findViewById(R.id.tv_address);
        tv_update = findViewById(R.id.tv_updates);
        tv_sensor = findViewById(R.id.tv_sensor);

        sw_gps  = findViewById(R.id.sw_gps);
        sw_locationUpdates = findViewById(R.id.sw_locationsupdates);

        //three source of location data
        // cell tower, wifi, and gps satellite
        //fused location API combines all three of this

        //set all properties of location request;
        //locationRequest = new LocationRequest();
        locationRequest = LocationRequest.create()
                .setInterval(1000* DEFAULT_UPDATE_INTERVAL) //how often does the location check occur?
                .setFastestInterval(1000* FAST_UPDATE_INTERVAL)         //how often does the location check occur when set to the most frequent update?

                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMaxWaitTime(100);

        sw_gps.setOnClickListener(View->{
            if(sw_gps.isChecked()){
                locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                tv_sensor.setText("using Gps sensor");
            }
            else {
                locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                tv_sensor.setText("Using TOWERS + WIFI");

            }
        });

        updateGps();
    }   //end of onCreate method

    //triggers a method after a location sharing permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGps();
                }
                else {
                    Toast.makeText(GPSTrackerDemo.this, "this app require permission to work properly", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void updateGps(){
        //get permission from the user to track GPS
        //get the current location from the fused client
        //update the UI

        //asked for location use permission
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GPSTrackerDemo.this);
        //check if the permission is granted
        if(ActivityCompat.checkSelfPermission(GPSTrackerDemo.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            System.out.println("113: pre permission granted");
            //when user provided permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(GPSTrackerDemo.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //we got permission. put the values
                    updateUIValues(location);
                }
            });
        }
        else {
            System.out.println("124: permission not granted");
            //permission not granted yet
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                System.out.println("127: asking permission");

                ActivityCompat.requestPermissions(GPSTrackerDemo.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_FINE_LOCATION);
            }
        }

    }

    private void updateUIValues(Location location) {
        System.out.println("location: "+location);
        //return;

        if (location != null){
            tv_lat.setText(String.valueOf (location.getLatitude()));
            tv_log.setText(String.valueOf (location.getLongitude()));
            tv_accuracy.setText(String.valueOf (location.getAccuracy()));

            if(location.hasAltitude()){
                tv_altitude.setText(String.valueOf(location.getAltitude()));
            }
            else tv_altitude.setText("Not Available");


            if(location.hasSpeed()){
                tv_speed.setText(String.valueOf(location.getSpeed()));
            }
            else tv_speed.setText("Not Available");
        } else {
            System.out.println("Location is null");
        }

    }
}