package com.example.demoapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.Preference;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private Toolbar mTopToolbar;
    Messenger mService;
    boolean bound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //Intent newIntent = new Intent(this,LocalService.class);
        //startService(newIntent);
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new MyPreferenceFragment()).commit();
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
                b.putString("ActivityName", "Main Activity");
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

    @Override
    protected void onDestroy() {
        //Intent newIntent = new Intent(this,LocalService.class);
        //stopService(newIntent);
        //Log.i(TAG,"Service stopped on destroy activity");
        super.onDestroy();
    }
}
