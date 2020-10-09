package com.example.demoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    static final String TAG = "GalleryActivity";
    Messenger mService;
    boolean bound;
    private List<ImageFile> imageFileList;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);
        verifyStoragePermissions(this);
        View view = findViewById(android.R.id.content).getRootView();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        view.setSystemUiVisibility(uiOptions);

        imageFileList = new ArrayList<ImageFile>(ImageFile.getImageFiles(this));
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        MyRecycleViewAdapter myRecycleViewAdapter = new MyRecycleViewAdapter(imageFileList);
        recyclerView.setAdapter(myRecycleViewAdapter);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
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
                b.putString("ActivityName", "Gallery Activity");
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
}