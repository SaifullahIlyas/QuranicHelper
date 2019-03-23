package com.example.quranichelper;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Any;

import java.util.*;

import static android.widget.Toast.LENGTH_LONG;

public class feedbackJ extends AppCompatActivity {
private TextToSpeech tts;
private EditText input;
Timer timer;
private SpeechRecognizer mSP;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back );
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        input= findViewById(R.id.messageFeed);
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
                   speeakVoice("Please Record You feedBack  ");
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
    protected   void  menuOPtion(View view)
    {

sendFeedBack();
    }
    public void sendFeedBack()
    {

        Map<String, Object> dictionary = new HashMap();
        dictionary.put("Date",new Date().toString());
        dictionary.put("feedBack",input.getText().toString());
        FirebaseFirestore ref = FirebaseFirestore.getInstance();
        ref.collection("FeedBack").document("6").set(dictionary);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                    if (error==6) {
                        speeakVoice("please Select the Right option");
                        try {
                            mSP.startListening(intent);
                        } catch (Exception e) {
                            Toast.makeText(feedbackJ.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResults(Bundle results) {
                  ArrayList<String>result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                  if(result!=null)
                  processResult(result.get(0).toString());
                  else
                      speeakVoice("result is null");
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                private void processResult(String mytring) {
                    input.setText(mytring);
                    speeakVoice(mytring);
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
    protected void onResume() {
        super.onResume();
        intilaizeEngine();
        getSpeachRecognizer();
        setInetent();
        mSP.startListening(intent);
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

    @Override
    protected void onPause() {
        super.onPause();
        tts.stop();
        tts.shutdown();
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
}

