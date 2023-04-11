package com.example.compass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

public class RuntimeAskPermissionTuto extends AppCompatActivity {

    private static final int REQUEST_LOCATION_CODE = 1234;
    TextView textView;
    Button ask_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_ask_permision_tuto);

        textView = findViewById(R.id.textView);
        ask_permission = findViewById(R.id.btn_ask_permision);

        //Check if location permission is granted or not
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //if granted
            textView.setText("Permission granted");
        } else { //if not granted
            textView.setText("Permission not granted");
        }

        ask_permission.setOnClickListener(this::askForPermission);
    }

    public void askForPermission(View view) {
        if (ContextCompat.checkSelfPermission(RuntimeAskPermissionTuto.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            //ask permission again
            if (ActivityCompat.shouldShowRequestPermissionRationale(RuntimeAskPermissionTuto.this,Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(RuntimeAskPermissionTuto.this)
                        .setMessage("We need permission for fine location")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(RuntimeAskPermissionTuto.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
                            }
                        })
                        .show();
            } else { //first time ask for permission
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
        } else {
            // permission is granted
            textView.setText("Permission granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //permission granted
                textView.setText("Permission granted");
            } else {    //permission not granted
                if (!ActivityCompat.shouldShowRequestPermissionRationale(RuntimeAskPermissionTuto.this,Manifest.permission.ACCESS_FINE_LOCATION)){
                    //permanently denied permission
                    new AlertDialog.Builder(RuntimeAskPermissionTuto.this)
                            .setMessage("You have permanently denied this permission, go to settings to enable this permission")
                            .setCancelable(false)
                            .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    gotoApplicationSettings();
                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .setCancelable(false)
                            .show();
                } else {
                    //
                    textView.setText("Permission not granted");
                }

            }

        }

    }

    private void gotoApplicationSettings(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}