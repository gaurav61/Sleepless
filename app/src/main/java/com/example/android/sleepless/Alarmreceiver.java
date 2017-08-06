package com.example.android.sleepless;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by gaurav on 12/7/17.
 */

public class Alarmreceiver extends BroadcastReceiver {
    NotificationCompat.Builder notification;
    private static final int UniqueID=12345;
    @Override
    public void onReceive(Context context, Intent intent) {
        notification=new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.owl2);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Sleepless");
        notification.setContentText("Click to Disable the alarm");
        Intent mintent=new Intent(context,MainActivity.class);
        PendingIntent pintent= PendingIntent.getActivity(context,0,mintent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pintent);
        NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(UniqueID,notification.build());
        context.startService(new Intent(context,MyService.class));
    }
}
