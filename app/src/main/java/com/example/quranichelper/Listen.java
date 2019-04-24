package com.example.quranichelper;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class Listen extends AppCompatActivity implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {
    ImageView playBtn, pauseBtn, StopBtn;
    MediaPlayer player;
    boolean playHandler=false;
    float intialpoint,finalPoint;
    GestureDetector gestureDetector;
    ProgressBar bar;
    boolean setprogreeses = false;
    CountDownTimer progressTimer;
    String s;
    static  int volume = 0;

    int i = 0;
    String status;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        playBtn = findViewById(R.id.playImag);
        pauseBtn = findViewById(R.id.pauseImg);
        StopBtn = findViewById(R.id.stopImg);
        Bundle intent = getIntent().getExtras();
        s = intent.getString("surahname");
        status = intent.getString("switch");
        Log.d("Status is",status);
        Log.d("iput",s);
        setPlayerDataSource(s,status);
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
                    setPlayerDataSource(s,status);
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

    }

    public void setPlayerDataSource(final String s,String status)
    {


        try {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            final FirebaseStorage storage = FirebaseStorage.getInstance();



            final   FirebaseFirestore  fs = FirebaseFirestore.getInstance();
            //DocumentReference ref = fs.collection("parah").document(s);
            if(status.length()<2)
            {
                DocumentReference  ref = fs.collection("parah").document(s);
                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        StorageReference stgRef = storage.getReference().child(documentSnapshot.getString("storageRef"));
                        Log.d("ref",stgRef.getDownloadUrl().toString());
                        stgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                try {
                                    player.setDataSource(Listen.this,uri);
                                    Log.d("dataSource",uri.toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                        fs.collection("lastlisten").document("last").update("name","You listen parah"+" "+s);
                                    }
                                });
                                player.prepareAsync();

                            }
                        });

                    }
                });

            }

            else if(status.length()>2) {
                DocumentReference ref = fs.collection("surah").document(s);
                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        StorageReference stgRef = storage.getReference().child(documentSnapshot.getString("storageRef"));
                        Log.d("ref",stgRef.getDownloadUrl().toString());
                        stgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                try {
                                    player.setDataSource(Listen.this,uri);
                                    Log.d("dataSource",uri.toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                        fs.collection("lastlisten").document("last").update("name","You listen surah"+" "+s);
                                    }
                                });
                                player.prepareAsync();

                            }
                        });

                    }
                });
            }


        } catch (Exception e) {
            Log.d("Exception", e.getLocalizedMessage());
        }
        //player.setDataSource("https://firebasestorage.googleapis.com/v0/b/fir-b9532.appspot.com/o/songs%2Fsong1.mp3?alt=media&token=a4424b28-93c3-4a0c-a6b9-9136dcf63335");
        // player.prepare();
    }// catch (Exception e) {
    // TODO: handle exception
    // }
    // }
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
                decreseVolume();
            }
            else
            {
                increaseVolume();
            }
        }
        return false;
    }

    private void decreseVolume() {

        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float percent = 0.1f;
        int tenVolume = (int) (maxVolume * percent);
        volume=currentVolume-tenVolume;
        if(volume>=0)
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        volume=0;
    }
    void increaseVolume()
    {
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float percent = 0.1f;
        int tenVolume = (int) (maxVolume * percent);
        volume=currentVolume+tenVolume;
        if(volume<=100)
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        volume=0;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void forword() {
        Log.d("mp3 forward"," song fordward");
        if(player.isPlaying()&&player.getCurrentPosition()<player.getDuration()-15000)
            player.seekTo(player.getCurrentPosition()+15000);
        else if(!player.isPlaying()&&player.getCurrentPosition()<player.getDuration()-15000) {
            player.seekTo(player.getCurrentPosition() + 15000);
        }


    }

    private void backward() {
        Log.d("mp3 backward"," Backward");
        if(player.isPlaying() && player.getCurrentPosition()>15000)
            player.seekTo(player.getCurrentPosition()-15000);
        else if(!player.isPlaying()&&player.getCurrentPosition()<player.getDuration()+15000) {
            player.seekTo(player.getCurrentPosition() - 15000);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setProgreess()
    {
        setColor();
        try {
            if( player !=null) {
                int curretposition = player.getCurrentPosition();
                int total = player.getDuration();
                int onePercent = total / 100;
                int remainig = total - curretposition;
                int used = total - remainig;
                final int progress = used / onePercent;
                if (player.isPlaying()) {
                    setprogreeses = true;
                    if(curretposition==total)
                        bar.setProgress(100);
                    bar.setProgress(progress);


                }
                else if(!player.isPlaying() && setprogreeses)
                {
                    if(curretposition==total)
                        bar.setProgress(100);
                    bar.setProgress(progress);
                }
            }



        }
        catch (Exception e)
        {
            Log.d("Error in progress",e.getLocalizedMessage());
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
    public  void playerUrl(String filename)//set player url
    {
        final FirebaseStorage storage = FirebaseStorage.getInstance();

        FirebaseFirestore fs;
        try {
            fs = FirebaseFirestore.getInstance();
            DocumentReference ref = fs.collection("parah30").document(filename);
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    StorageReference stgRef = storage.getReference().child(documentSnapshot.getString("storageRef"));
                    Log.d("ref",stgRef.getDownloadUrl().toString());
                    stgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                player.setDataSource(Listen.this,uri);
                                Log.d("dataSource",uri.toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                }
                            });
                            player.prepareAsync();
                        }
                    });

                }
            });

        } catch (Exception e) {
            Log.d("Exception", e.getLocalizedMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.stop();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
