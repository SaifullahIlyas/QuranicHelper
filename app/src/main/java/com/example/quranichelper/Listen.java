package com.example.quranichelper;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class Listen extends AppCompatActivity implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {
    ImageView playBtn, pauseBtn, StopBtn, downloadBtn;
    MediaPlayer player;
    boolean playHandler=false;
    float intialpoint,finalPoint;
    GestureDetector gestureDetector;
    ProgressBar bar;
    boolean setprogreeses = false;
    CountDownTimer progressTimer;
    Intent intent = getIntent();
    String s = intent.getStringExtra("surahname");

    int i = 0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        playBtn = findViewById(R.id.playImag);
        pauseBtn = findViewById(R.id.pauseImg);
        StopBtn = findViewById(R.id.stopImg);
        downloadBtn = findViewById(R.id.downloadImg);
        setPlayerDataSource();
        gestureDetector = new GestureDetector(this);
        bar = findViewById(R.id.songProgrsess);
        bar.setProgress(100);

        bar.setProgressTintList(ColorStateList.valueOf(Color.WHITE));

        progressTimer = new CountDownTimer(1000000000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

                setProgreess();
            }

            @Override
            public void onFinish() {

            }
        }.start();

        pauseBtn.setEnabled(false);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseBtn.setEnabled(true);
                if(!playHandler)
                setPlayerDataSource();
                player.start();
                playBtn.setEnabled(false);
                bar.setProgress((player.getDuration()));
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
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgreess();
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

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float swipey = e2.getY() - e1.getY();
        float swipex  = e2.getX()- e1.getX();
        if(Math.abs(swipex)>Math.abs(swipey))//swipe in x direction
        {
            if(swipex>0)
            {
                forword();
            }
            else
            {
                backward();
            }
        }
        else
        {
            if(swipey>0)
            {
                download();
            }
            else
            {
                //this is nothti
            }
        }
        return false;
    }

    private void download() {
        Log.d("mp3 download"," Download");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void forword() {
        Log.d("mp3 forward"," song fordward");
        if(player.isPlaying()&&player.getCurrentPosition()<player.getDuration()-5000)
        player.seekTo(player.getCurrentPosition()+5000);
        else if(!player.isPlaying()&&player.getCurrentPosition()<player.getDuration()-5000) {
            player.seekTo(player.getCurrentPosition() + 5000);
        }


    }

    private void backward() {
        Log.d("mp3 backward"," Backward");
        if(player.isPlaying() && player.getCurrentPosition()>5000)
            player.seekTo(player.getCurrentPosition()-5000);
        else if(!player.isPlaying()&&player.getCurrentPosition()<player.getDuration()+5000) {
            player.seekTo(player.getCurrentPosition() - 5000);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setProgreess()
    {
        setColor();
        if( player !=null) {
            int curretposition = player.getCurrentPosition();
            int total = player.getDuration();
            int onePercent = total / 100;
            int remainig = total - curretposition;
            int used = total - remainig;
            int progress = used / onePercent;
            if (player.isPlaying()) {
                setprogreeses = true;
                if(curretposition==total)
                bar.setProgress(100);
                bar.setProgress(progress);
                //calculateTime();

            }
            else if(!player.isPlaying() && setprogreeses)
            {
                if(curretposition==total)
                    bar.setProgress(100);
                bar.setProgress(progress);
            }


        }
    }
    public void switchBetweenStates()
    {
      if(player.isPlaying())
          player.pause();
      else
      {
          player.start();
      }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        switchBetweenStates();
        Log.d("doubleClick","double clicked");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {

        return false;
    }

    public void calculateTime()
    {
        String timeStr=null;
        Double timeInSecond =Double.valueOf(player.getDuration()/1000) ;
        timeStr = timeInSecond.toString();
        if(timeInSecond>60)
        {
           if(timeInSecond%60!=0)
           {
               timeStr = String.valueOf(Math.floor(timeInSecond/60))+":"+String.valueOf(timeInSecond%60);
           }

        }
        Toast.makeText(this,timeStr,Toast.LENGTH_LONG).show();

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setColor()
    {
        if(player.isPlaying())
            bar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        else if (!player.isPlaying() && bar.getProgress()==100)
            bar.setProgressTintList(ColorStateList.valueOf(Color.WHITE));
    }
}