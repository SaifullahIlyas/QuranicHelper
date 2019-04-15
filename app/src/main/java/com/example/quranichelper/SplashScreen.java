package com.example.quranichelper;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.common.collect.ComparisonChain.start;

public class SplashScreen extends AppCompatActivity {
    MediaPlayer player;
    ImageView img;
    RelativeLayout layout;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        layout = findViewById(R.id.splashParent);
       player =  MediaPlayer.create(SplashScreen.this,R.raw.bismillah);
       player.start();
        TextView textView = findViewById(R.id.applabel);
         ImageView imageView = findViewById(R.id.audioanimation);
         ImageView moveImage=  findViewById(R.id.moveImage);
        final boolean isAvalibale = isNetworkAvailable();


        

       /* Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        imageView.startAnimation(animation);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", 200f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 300f);
        ObjectAnimator.ofPropertyValuesHolder(moveImage, pvhX, pvhY).setDuration(5000).start();*/



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isAvalibale) {
                    Intent intent = new Intent(SplashScreen.this, FingerPrintLock.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this,NetworError.class);
                    startActivity(intent);
                    finish();
                }

            }
        },7000);
    }
    private int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }
    public void setPlayerDataSource()
    {

        try {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource("C:\\Users\\Saifuu\\Downloads\\Music\\bismillah");

            player.prepare();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();

    }

}
