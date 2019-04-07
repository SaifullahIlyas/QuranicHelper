package com.example.quranichelper;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    MediaPlayer player;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        img = findViewById(R.id.bissmillahimg);

       setPlayerDataSource();
       player.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,FingerPrintLock.class);
                startActivity(intent);
                finish();
            }
        },1000000);
    }
    public void setPlayerDataSource()
    {

        try {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource("https://firebasestorage.googleapis.com/v0/b/fir-b9532.appspot.com/o/songs%2Fsong1.mp3?alt=media&token=a4424b28-93c3-4a0c-a6b9-9136dcf63335");
            player.prepare();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
