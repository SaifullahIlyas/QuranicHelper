package com.example.quranichelper;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class Rate extends AppCompatActivity  {
RatingBar ratingBar;
CardView submitt;
SpeechRecognizer mSP;
Intent intent;
TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        RelativeLayout ratelaypoy = findViewById(R.id.ratelayout);
        ratelaypoy.setLongClickable(true);
        ratelaypoy.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mSP.startListening(intent);
                return  true;
            }
        });
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
               speeakVoice("we are sending your Rating");
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
    public  void getSpeachRecognizer()//set recognition engine
    {
        if(SpeechRecognizer.isRecognitionAvailable(this))
        {
            mSP = SpeechRecognizer.createSpeechRecognizer(this);
            mSP.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onError(int error) {


                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if(result!=null) {
                        processResult(result.get(0).toString());
                        if(result.get(0).contains("5"));
                    }
                    else
                        speeakVoice("result is null");
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                private void processResult(String mytring) {
                    Double time =0.0;
                    ratingBar.setRating(Float.valueOf(mytring));

                    speeakVoice("Double Tap on Screen To send Your feedback");


                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }

    }





    @Override
    protected void onDestroy() {

        super.onDestroy();
        if(tts!=null)
        {
            tts.stop();
            tts.shutdown();
        }
        mSP.stopListening();
        mSP.cancel();
        mSP.destroy();
    }

    public void setInetent()//set intent
    {
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_RESULTS,1);


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


    @Override
    protected void onResume() {
        super.onResume();
        intilaizeEngine();
        getSpeachRecognizer();
        setInetent();

    }

    @Override
    protected void onPause() {
        super.onPause();
        tts.stop();
        tts.shutdown();
        mSP.stopListening();
        mSP.cancel();

    }
}
