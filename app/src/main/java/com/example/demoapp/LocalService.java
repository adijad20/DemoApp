package com.example.demoapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Random;

public class LocalService extends Service {
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private static final String TAG = "MyService";
    static final int MSG_GET_ACTIVITY = 1;
    private NotificationManager mNotifyManager;
    Messenger mMessenger;
    private final Random mGenerator = new Random();


    static class IncomingHandler extends Handler{
        private Context applicationContext;
        public IncomingHandler(Context context){
            applicationContext = context.getApplicationContext();
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MSG_GET_ACTIVITY:
                    //Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show();
                    String activityName = msg.getData().getString("ActivityName");
                    Log.i(TAG,"Message received: "+activityName);
                    showWindow(activityName, applicationContext);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        Log.i(TAG,"Connection is made");
        mMessenger = new Messenger(new IncomingHandler(this));
        return mMessenger.getBinder();
    }

    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Local Service started", Toast.LENGTH_SHORT).show();
        Log.i("Myservice","Service started");
        createNotificationChannel();
        sendNotification();
        return START_NOT_STICKY;
    }
     */

    public int getRandomNumber(){
        return mGenerator.nextInt(100);
    }

    public void createNotificationChannel() {
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Service Notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notification from Service");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,
                notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Service started")
                .setContentText("Notification from local service")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingNotificationIntent)
                .setAutoCancel(true);
        return notifyBuilder;
    }

    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showWindow(String activityName, Context context){
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        final WindowManager.LayoutParams layOutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        layOutParams.gravity = Gravity.END;
        layOutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //layOutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layOutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        LayoutInflater layOutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View viewAboveAllActivities = layOutInflater.inflate(R.layout.activity_flash_alert_layout, null);
        TextView textView = viewAboveAllActivities.findViewById(R.id.alertView);
        textView.setText("You are in "+activityName+"\nTouch to dismiss");
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                windowManager.removeViewImmediate(viewAboveAllActivities);
                return false;
            }
        });

        windowManager.addView(viewAboveAllActivities, layOutParams);
    }
}
