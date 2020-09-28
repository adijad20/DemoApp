package com.example.demoapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class FragmentActivity extends AppCompatActivity implements FragmentOne.OnButtonSelectedListener {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout);
    }

    public void onButtonSelected(int opt){
        Log.i("Myactivity","inside main activity, option received: "+Integer.toString(opt));
        FragmentTwo fragTwo = (FragmentTwo) getSupportFragmentManager().findFragmentById(R.id.layout_two);
        Bundle bundle = new Bundle();
        bundle.putInt("SEL_OPT",opt);
        //bundle.putString("SEL_OPT",str);
        fragTwo.putArguments(bundle);
        Log.i("Myactivity","Successfully got fragtwo");
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if(fragment instanceof FragmentOne){
            FragmentOne fragOne =(FragmentOne) fragment;
            fragOne.setOnButtonSelectedListener(this);
        }
    }
}
