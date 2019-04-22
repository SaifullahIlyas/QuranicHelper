package com.example.quranichelper;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Any;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_LONG;

public class feedbackJ extends AppCompatActivity {
private TextToSpeech tts;
private EditText input;
Timer timer;
boolean sent =false;
private SpeechRecognizer mSP;
LinearLayout feedBackRecord;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back );

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setTitle("User Feedback");
        input= findViewById(R.id.messageFeed);
        feedBackRecord = findViewById(R.id.feedBackRecord);
        feedBackRecord.setLongClickable(true);
        feedBackRecord.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                speeakVoice("ok we are Sending your feedBack");
              sendFeedBack();
            }
        });
        feedBackRecord.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                speeakVoice("Record feedback");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSP.startListening(intent);
                    }
                },500);
                return  true;
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
                   speeakVoice("Hold on Screen to record feedBack  ");
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
input.setText("");
    }
    public void sendFeedBack()
    {
        String s  =  getEmail();

        Map<String, Object> dictionary = new HashMap();
        dictionary.put("Date",new Date().toString());
        dictionary.put("feedBack",input.getText().toString());
        final FirebaseFirestore ref = FirebaseFirestore.getInstance();

        if(input.getText().length()>0) {
            ref.collection("Feedback").document(s).set(dictionary).addOnSuccessListener(new OnSuccessListener<Void>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onSuccess(Void aVoid) {
                    speeakVoice("FeedBack Sent SuccessFully");
                    ref.collection("feednotification").document("notify").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Long  val = Long.valueOf( documentSnapshot.getLong("value"));
                            val = val+1;
                            ref.collection("feednotification").document("notify").update("value",val);
                        }
                    });
                    mSP.cancel();
                    mSP.stopListening();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onFailure(@NonNull Exception e) {
                    speeakVoice("THere is no internet connection Availabale");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSP.startListening(intent);
                        }
                    }, 3000);
                }
            });
        }
        else
        {
            speeakVoice("feedback must not be empty");
        }
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


                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResults(Bundle results) {
                  ArrayList<String>result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                  if(result!=null) {
                      processResult(result.get(0).toString());
                  }
                  else
                      speeakVoice("result is null");
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                private void processResult(String mytring) {
                    Double time =0.0;
                    input.setText(mytring);

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
    protected void onResume() {
        super.onResume();
        intilaizeEngine();
        tts.setSpeechRate((float)1.0);
        getSpeachRecognizer();
        setInetent();


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

    String getEmail()
    {
        String email = null;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();

        for (Account account:accounts) {
            if(pattern.matcher(account.name).matches()) {
                email = account.name;

            }

        }
        Log.d("email",email);
        return email;
    }

}

