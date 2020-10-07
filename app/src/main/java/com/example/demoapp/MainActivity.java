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
    private Toolbar mTopToolbar;
    Messenger mService;
    boolean bound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new MyPreferenceFragment()).commit();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = new Messenger(iBinder);
            bound = true;
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
        //Intent newIntent = new Intent(this,LocalService.class);
        //startService(newIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bound) {
            Bundle b = new Bundle();
            b.putString("ActivityName", this.getClass().getSimpleName());
            Message msg = Message.obtain(null, LocalService.MSG_GET_ACTIVITY, 0, 0);
            msg.setData(b);
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Intent newIntent = new Intent(this,LocalService.class);
        stopService(newIntent);
        super.onDestroy();
    }
}
