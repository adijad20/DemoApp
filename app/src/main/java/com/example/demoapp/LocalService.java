package com.example.demoapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Random;

public class LocalService extends Service {
    private final IBinder binder = new LocalBinder();
    private final Random mGenerator = new Random();
    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        Log.i("Myservice","Connection is made");
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.i("Myservice","Service started");
        return START_NOT_STICKY;
    }

    public int getRandomNumber(){
        return mGenerator.nextInt(100);
    }

}
