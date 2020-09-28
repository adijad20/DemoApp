package com.example.demoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Broadcast received");
        String action = intent.getAction();
        Intent serviceIntent = new Intent(context,LocalService.class);
        context.startService(serviceIntent);
    }
}
