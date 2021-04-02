package com.vinay.sensordatalogging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;




import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends WearableActivity {
    final String TAG = "SensorLog";
    private TextView mTextView;
    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener;
    private float ax;
    private float ay;
    private float az;
    private float gx;
    private float gy;
    private float gz;
    private float px;
    private float si;
    private long sensorTimeReference = 0l;
    private long myTimeReference = 0l;
    FileWriter writer;
    private NotificationManager nNM;
    private TriggerEventListener triggerEventListener;


   // @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextView = findViewById(R.id.text);

        //Saving data to csv
        Log.d(TAG ,"Writing to " + getStorageDir());
        try {
            writer = new FileWriter(new File(getStorageDir(), "sensor" + System.currentTimeMillis() + ".csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mSensorManager = (SensorManager) this
                .getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }


            @Override
            public void onSensorChanged(SensorEvent event) {
                Sensor sensor = event.sensor;
                    if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                        ax = event.values[0];
                        ay = event.values[1];
                        az = event.values[2];
                    }
                    if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                        //TODO: get values
                        gx = event.values[0];
                        gy = event.values[1];
                        gz = event.values[2];
                    }

                    /*if (sensor.getType() == Sensor.TYPE_PRESSURE) {
                        //TODO: get values
                        px = event.values[0];
                    }*/
                /*if(sensorTimeReference == 0l && myTimeReference == 0l) {
                    sensorTimeReference = event.timestamp;
                    myTimeReference = System.currentTimeMillis();
                }
                event.timestamp = myTimeReference +
                        Math.round((event.timestamp - sensorTimeReference) / 1000000.0);*/
                try {
                    writer.write(String.format("%d, %f, %f, %f, %f, %f, %f\n", event.timestamp, ax, ay, az, gx , gy, gz));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

       /* triggerEventListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {
                // Do work
            }
        };*/


        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION), SensorManager.SENSOR_DELAY_NORMAL);


        // Enables Always-on
        setAmbientEnabled();
    }


    private String getStorageDir() {
        return this.getExternalFilesDir(null).getAbsolutePath();
    }
}

