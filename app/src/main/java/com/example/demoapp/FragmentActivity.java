package com.example.demoapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class FragmentActivity extends AppCompatActivity implements FragmentOne.OnButtonSelectedListener {
    static final String TAG = "FragmentActivity";
    Messenger mService;
    boolean bound;
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
        bound = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = new Messenger(iBinder);
            bound = true;
            Log.i(TAG,"Bound to the service");
            Log.i(TAG,"Trying to message to the service, bound: "+bound);
            if(bound) {
                Bundle b = new Bundle();
                b.putString("ActivityName", "Fragment Activity");
                //Log.i(TAG,"Activity name: "+this.getClass().getSimpleName());
                Message msg = Message.obtain(null, LocalService.MSG_GET_ACTIVITY, 0, 0);
                msg.setData(b);
                try {
                    mService.send(msg);
                    Log.i(TAG,"Message sent to the service");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
            bound = false;
        }
    };
}
