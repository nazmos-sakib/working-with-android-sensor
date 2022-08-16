package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class compass2 extends AppCompatActivity  implements SensorEventListener {

    private ImageView comArrow;

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currentAzimuth = 0f;

    private SensorManager mSensorManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass2);

        comArrow = findViewById(R.id.imageView10);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener((SensorEventListener) this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener((SensorEventListener) this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME);

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float alpha = 0.9f;
        synchronized (this){
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                mGravity[0] = alpha *mGravity[0]+(1-alpha)*sensorEvent.values[0];
                mGravity[1] = alpha *mGravity[1]+(1-alpha)*sensorEvent.values[1];
                mGravity[2] = alpha *mGravity[2]+(1-alpha)*sensorEvent.values[2];
            }

            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                mGeomagnetic[0] = alpha *mGeomagnetic[0]+(1-alpha)*sensorEvent.values[0];
                mGeomagnetic[1] = alpha *mGeomagnetic[1]+(1-alpha)*sensorEvent.values[1];
                mGeomagnetic[2] = alpha *mGeomagnetic[2]+(1-alpha)*sensorEvent.values[2];
            }

            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnetic);
            if (success){
                float orientation[] = new float[3];
                SensorManager.getOrientation(R,orientation);
                azimuth = (float) Math.toDegrees(orientation[0]);
                azimuth = (azimuth+360)%360;

                Animation anim = new RotateAnimation(-currentAzimuth,-azimuth,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                currentAzimuth = azimuth;
                anim.setDuration(500);
                anim.setRepeatCount(0);
                anim.setFillAfter(true);
                comArrow.startAnimation(anim);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}