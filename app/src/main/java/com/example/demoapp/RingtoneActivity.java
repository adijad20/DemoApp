package com.example.demoapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RingtoneActivity extends AppCompatActivity {
    Map<String,String> ringtoneMap;
    ArrayList<String> ringtoneList;
    private MediaPlayer mediaPlayer;
    Messenger mService;
    boolean bound;
    static final String TAG = "RingtoneActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringtone_layout);
        Log.i(TAG,"Inside onCreate");
        ringtoneMap = new HashMap<String, String>(getNotifications());
        ringtoneList = new ArrayList<String>(ringtoneMap.keySet());

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, ringtoneList);

        ListView listView = findViewById(R.id.ringtone_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getAdapter().getItem(i);
                Log.i("MyListView",(String)ringtoneMap.get(selected));
                Uri uri = Uri.parse((String)ringtoneMap.get(selected));
                if (mediaPlayer != null)
                    mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                );
                try {
                    mediaPlayer.setDataSource(getApplicationContext(),uri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });
    }

    public Map<String, String> getNotifications() {
        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = manager.getCursor();
        Map<String, String> list = new HashMap<>();
        while (cursor.moveToNext()) {
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            list.put(notificationTitle, notificationUri);
            //RingtoneManager.getRingtone(this, Uri.parse(notificationUri));
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
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
            b.putString("ActivityName", "Ringtone Activity");
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
        Log.i(TAG,"Inside onstart");
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        bound = false;
    }
}

