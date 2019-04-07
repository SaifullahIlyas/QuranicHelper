package com.example.quranichelper;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Rate extends AppCompatActivity  {
RatingBar ratingBar;
CardView submitt;
TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ratingBar = findViewById(R.id.rating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this, R.color.blue), PorterDuff.Mode.SRC_ATOP);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               speeakVoice("You rates " +String.valueOf( ratingBar.getRating())+" out of 5");
            }
        });
        submitt = findViewById(R.id.submitRate);
       findViewById(R.id.ratelayout) .setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {

              submitt();
            }
        });
        submitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  submitt();
            }
        });
    }
    public void intilaizeEngine()//this is the code to initilize Text to Speach
    {

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {

                if(tts.getEngines().size()==0)
                {

                }
                else {
                    tts.setLanguage(Locale.ENGLISH);
                    speeakVoice("Rate This Application  ");
                }
               /* if (status==TextToSpeech.SUCCESS)
                {
                    int result =tts.setLanguage(Locale.ENGLISH);
                    if(result ==TextToSpeech.LANG_MISSING_DATA|| result== TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.e("tts","Language is not supported");
                    }
                }*/

            }
        });
    }
    public  void speeakVoice(String mess)//code to speak input
    {
        //String speakMeassage=input.getText().toString();
        tts.speak(mess,TextToSpeech.QUEUE_FLUSH,null,null);

    }
    void submitt()
    {
        if(ratingBar.getRating()==0)
            speeakVoice("minimum rating should be 1.please rate");
        else {

            speeakVoice("We are sending your Rating");
            Map<String, Object> dictionary = new HashMap();
            dictionary.put("Rating", String.valueOf(ratingBar.getRating()));
            FirebaseFirestore ref = FirebaseFirestore.getInstance();
            ref.collection("Rating").document("7").set(dictionary).addOnSuccessListener(new OnSuccessListener<Void>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onSuccess(Void aVoid) {
                    speeakVoice("Thank you for your contribution");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }


    public abstract class DoubleClickListener implements View.OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);
        public abstract void onDoubleClick(View v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intilaizeEngine();

    }

    @Override
    protected void onPause() {
        super.onPause();
        tts.stop();
    }
}
