package com.example.demoapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Broadcast received");
        Toast.makeText(context, "Boot completed received!", Toast.LENGTH_SHORT).show();
        createNotificationChannel(context);
        sendNotification(context);
        Intent mainIntent = new Intent(context,MainActivity.class);
        context.startActivity(mainIntent);
        //String action = intent.getAction();
        //Intent serviceIntent = new Intent(context,LocalService.class);
        //context.startService(serviceIntent);
    }

    public void createNotificationChannel(Context context) {
        mNotifyManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Broadcast Notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notification from Broadcast receiver");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(Context context){
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, NOTIFICATION_ID,
                notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setContentTitle("Boot completed broadcast received")
                .setContentText("Notification from Broadcast receiver")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingNotificationIntent)
                .setAutoCancel(true);
        return notifyBuilder;
    }

    public void sendNotification(Context context){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(context);
        mNotifyManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }

}
