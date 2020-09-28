package com.example.demoapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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
    private Sensor mSensorProximity;
    private Sensor mSensorLight;
    private float sensorReading;
    private TextView mTextSensor;
    private int option;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_two, container, false);
        Log.i("Myactivity","in fragment 2");
        mTextSensor = view.findViewById(R.id.viewSensor);
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
        if(option == 1 && sensorType == Sensor.TYPE_PROXIMITY)
            mTextSensor.setText(getResources().getString(R.string.show_sensor,sensorReading));
        if(option == 2 && sensorType == Sensor.TYPE_LIGHT)
            mTextSensor.setText(getResources().getString(R.string.show_sensor,sensorReading));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void putArguments(Bundle args){
        option = args.getInt("SEL_OPT");
        mTextSensor.setText(getResources().getString(R.string.show_sensor,0.00));
        //String strtext = args.getString("SEL_OPT");
        Log.i("Myactivity","Inside fragtwo: option received is "+Integer.toString(option));

        //TextView textView = getView().findViewById(R.id.textView);
        //textView.setText(strtext);
        //Log.i("Myactivity",strtext);

    }
}

