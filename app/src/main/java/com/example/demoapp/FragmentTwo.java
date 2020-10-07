package com.example.demoapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class FragmentTwo extends Fragment implements SensorEventListener {
    private SensorManager mSensorManager;
    private Vibrator mVibrator;
    private Sensor mSensorProximity;
    private Sensor mSensorLight;
    private Sensor mSensorAccel;
    private Sensor mSensorMagnet;
    private Sensor mSensorGyro;
    private float sensorReading;
    private float sensorReading1;
    private  float sensorReading2;
    private TextView mTextSensor;
    private TextView mTextSensor1;
    private TextView mTextSensor2;
    private int option;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnet = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_two, container, false);
        Log.i("Myactivity","in fragment 2");
        mTextSensor = view.findViewById(R.id.viewSensor);
        mTextSensor1 = view.findViewById(R.id.viewSensor2);
        mTextSensor2 = view.findViewById(R.id.viewSensor3);

        mTextSensor1.setVisibility(View.INVISIBLE);
        mTextSensor2.setVisibility(View.INVISIBLE);
        /*
        String strtext = getArguments().getString("SEL_OPT");
        Log.i("Myactivity","Inside fragment 2, string received: "+strtext);
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(strtext);
        */
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mSensorProximity != null)
            mSensorManager.registerListener(this,mSensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        if(mSensorLight != null)
            mSensorManager.registerListener(this, mSensorLight,SensorManager.SENSOR_DELAY_NORMAL);
        if(mSensorAccel != null)
            mSensorManager.registerListener(this, mSensorAccel,SensorManager.SENSOR_DELAY_NORMAL);
        if(mSensorMagnet != null)
            mSensorManager.registerListener(this, mSensorMagnet,SensorManager.SENSOR_DELAY_NORMAL);
        if(mSensorGyro != null)
            mSensorManager.registerListener(this, mSensorGyro,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        sensorReading = sensorEvent.values[0];
        if(option == 1 && sensorType == Sensor.TYPE_PROXIMITY) {
            mTextSensor.setText(getResources().getString(R.string.show_sensor, "Proximity",sensorReading));
        }
        if(option == 2 && sensorType == Sensor.TYPE_LIGHT)
            mTextSensor.setText(getResources().getString(R.string.show_sensor, "Ambient light",sensorReading));

        if(option == 3 && sensorType == Sensor.TYPE_ACCELEROMETER) {
            sensorReading1 = sensorEvent.values[1];
            sensorReading2 = sensorEvent.values[2];
            mTextSensor.setText(getResources().getString(R.string.show_dir_sensor, "Accelerometer","X",sensorReading));

            if(mTextSensor1.getVisibility() == View.INVISIBLE)
                mTextSensor1.setVisibility(View.VISIBLE);
            if(mTextSensor2.getVisibility() == View.INVISIBLE)
                mTextSensor2.setVisibility(View.VISIBLE);

            mTextSensor1.setText(getResources().getString(R.string.show_dir_sensor,"Accelerometer","Y", sensorReading1));
            mTextSensor2.setText(getResources().getString(R.string.show_dir_sensor, "Accelerometer","Z",sensorReading2));
        }
        if(option == 4 && sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            sensorReading1 = sensorEvent.values[1];
            sensorReading2 = sensorEvent.values[2];

            mTextSensor.setText(getResources().getString(R.string.show_dir_sensor, "Magnetometer","X",sensorReading));

            if(mTextSensor1.getVisibility() == View.INVISIBLE)
                mTextSensor1.setVisibility(View.VISIBLE);
            if(mTextSensor2.getVisibility() == View.INVISIBLE)
                mTextSensor2.setVisibility(View.VISIBLE);

            mTextSensor1.setText(getResources().getString(R.string.show_dir_sensor, "Magnetometer","Y",sensorReading1));
            mTextSensor2.setText(getResources().getString(R.string.show_dir_sensor, "Magnetometer","Z",sensorReading2));
        }
        if(option == 5 && sensorType == Sensor.TYPE_GYROSCOPE) {
            sensorReading1 = sensorEvent.values[1];
            sensorReading2 = sensorEvent.values[2];
            mTextSensor.setText(getResources().getString(R.string.show_dir_sensor, "Gyroscope","X",sensorReading));

            if (mTextSensor1.getVisibility() == View.INVISIBLE)
                mTextSensor1.setVisibility(View.VISIBLE);
            if (mTextSensor2.getVisibility() == View.INVISIBLE)
                mTextSensor2.setVisibility(View.VISIBLE);

            mTextSensor1.setText(getResources().getString(R.string.show_dir_sensor,"Gyroscope","Y",sensorReading1));
            mTextSensor2.setText(getResources().getString(R.string.show_dir_sensor, "Gyroscope","Z",sensorReading2));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void putArguments(Bundle args){
        option = args.getInt("SEL_OPT");
        mTextSensor1.setVisibility(View.INVISIBLE);
        mTextSensor2.setVisibility(View.INVISIBLE);
        if(option == 6) {
            mTextSensor.setText("Vibrated for 2 seconds!");
            mVibrator.vibrate(2000);
            //mTextSensor.setText(R.string.intro_text);
        }
        Log.i("Myactivity","Inside fragtwo: option received is "+Integer.toString(option));



    }
}

