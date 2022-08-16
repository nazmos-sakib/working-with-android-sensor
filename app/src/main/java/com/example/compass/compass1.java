package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class compass1 extends AppCompatActivity {

    private ImageView imgCompass;

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagneticField;

    private float[] floatGravity = new float[3];
    private float[] floatGeoMagnetic = new float[3];
    private float[] floatOrientation = new float[3];
    private float[] floatRotationMatrix = new float[9];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass1);

        imgCompass = findViewById(R.id.imgCompass);

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(View->{
            this.resetImage();
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        SensorEventListener sensorEventListenerAccelerometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                floatGravity = sensorEvent.values;

                SensorManager.getRotationMatrix(floatRotationMatrix,null,floatGravity,floatGeoMagnetic);
                SensorManager.getOrientation(floatRotationMatrix,floatOrientation);

                imgCompass.setRotation((float) (floatOrientation[0]*180/3.14159));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        SensorEventListener sensorEventListenerMagneticField = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                floatGeoMagnetic = sensorEvent.values;

                SensorManager.getRotationMatrix(floatRotationMatrix,null,floatGravity,floatGeoMagnetic);
                SensorManager.getOrientation(floatRotationMatrix,floatOrientation);

                imgCompass.setRotation((float) (floatOrientation[0]*180/3.14159));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(sensorEventListenerAccelerometer, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListenerMagneticField, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);

    }
    public void resetImage(){
        imgCompass.setRotation(180.0f);
    }

}