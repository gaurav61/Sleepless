package com.example.android.sleepless;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

/**
 * Created by gaurav on 13/7/17.
 */

public class MyService extends Service {
    private MediaPlayer player;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player= MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
        Runnable r=new Runnable() {
            @Override
            public void run() {
                player.start();
            }
        };
        Thread t=new Thread(r);
        t.start();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
        player.stop();
    }


}
