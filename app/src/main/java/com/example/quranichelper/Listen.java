package com.example.quranichelper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Listen extends AppCompatActivity {
    ImageView playBtn, pauseBtn, StopBtn, downloadBtn;
    MediaPlayer player;
    boolean playHandler=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        playBtn = findViewById(R.id.playImag);
        pauseBtn = findViewById(R.id.pauseImg);
        StopBtn = findViewById(R.id.stopImg);

        pauseBtn.setEnabled(false);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseBtn.setEnabled(true);
                if(!playHandler)
                setPlayerDataSource();
                player.start();
                playBtn.setEnabled(false);
                playHandler = false;


            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pause();
            }
        });
        StopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayer();
            }
        });


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
    public void Pause()
    {
        if(player.isPlaying()) {
            player.pause();
            playBtn.setEnabled(true);
            playHandler =true;
        }
    }
    public  void stopPlayer()
    {
        if(player.isPlaying()) {
            player.stop();
            playBtn.setEnabled(true);
        }
    }
}