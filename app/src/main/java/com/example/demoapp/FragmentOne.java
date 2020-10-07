package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentOne extends Fragment implements View.OnClickListener{
    OnButtonSelectedListener callback;

    public interface OnButtonSelectedListener {
        public void onButtonSelected(int opt);
    }

    public void setOnButtonSelectedListener(OnButtonSelectedListener callback){
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_one, container, false);
        Button one = (Button) view.findViewById(R.id.proximitySensor);
        one.setOnClickListener(this);
        Button two = (Button) view.findViewById(R.id.lightSensor);
        two.setOnClickListener(this);
        Button three = (Button) view.findViewById(R.id.accelSensor);
        three.setOnClickListener(this);
        Button four = (Button) view.findViewById(R.id.magnetSensor);
        four.setOnClickListener(this);
        Button five = (Button) view.findViewById(R.id.gyroSensor);
        five.setOnClickListener(this);
        Button six = (Button) view.findViewById(R.id.vibrate);
        six.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.proximitySensor:
                String str1 = "Sensor 1 clicked";
                Log.i("Myactivity","Sensor 1 clicked");
                callback.onButtonSelected(1);
                break;
            case R.id.lightSensor:
                String str2 = "Sensor 2 clicked";
                Log.i("Myactivity","Sensor 2 clicked");
                callback.onButtonSelected(2);
                break;
            case R.id.accelSensor:
                callback.onButtonSelected(3);
                break;
            case R.id.magnetSensor:
                callback.onButtonSelected(4);
                break;
            case R.id.gyroSensor:
                callback.onButtonSelected(5);
                break;
            case R.id.vibrate:
                callback.onButtonSelected(6);
                break;
            default:
                String str_def = "Default string";
                Log.i("Myactivity",str_def);
                break;
        }
    }
}