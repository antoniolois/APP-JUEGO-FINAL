package com.example.smash_topo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundMusic extends Service {
    MediaPlayer myPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {

        myPlayer = MediaPlayer.create(this,R.raw.backoung_music);
        //setting loop play to true
        //this will make the ringtone continuously playing
        myPlayer.setLooping(true); // Set looping
    }
    @Override
    public void onStart(Intent intent, int startid) {
        myPlayer.start();
    }
    @Override
    public void onDestroy() {
        myPlayer.stop();
    }
}