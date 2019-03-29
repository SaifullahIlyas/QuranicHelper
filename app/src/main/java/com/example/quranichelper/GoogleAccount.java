package com.example.quranichelper;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class GoogleAccount extends AppCompatActivity {
 GoogleSignInClient mGoogleSignInClient;
 Intent intent;
    private TextToSpeech tts;
    private EditText input;
    Timer timer;
    boolean sent =false;
    private SpeechRecognizer mSP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_account);
        TextView tt =  findViewById(R.id.email);
        try {
            String androidId = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);


            tt.setText(androidId.toString());
        }
        catch (Exception e)
        {
           tt.setText(e.getLocalizedMessage().toString());
        }

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
        ref.collection("Feedback").document("7").set(dictionary).addOnSuccessListener(new OnSuccessListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSuccess(Void aVoid) {
                speeakVoice("FeedBack Sent SuccessFully");
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
                },3000);
            }
        });
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
                    if (error==6 && sent==false) {
                        speeakVoice("please  select option");
                        try {
                            mSP.startListening(intent);
                        } catch (Exception e) {
                            Toast.makeText(GoogleAccount.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if(result!=null) {
                        processResult(result.get(0).toString());
                    }
                    else
                        speeakVoice("result is null");
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                private void processResult(String mytring) {
                    Double time =0.0;
                    if (!mytring.contains("yes") && mytring.length()>5)
                    {
                        input.setText(mytring);
                    }

                    if (mytring.contains("yes")&& mytring.length()<10) {

                        sendFeedBack();
                        speeakVoice("ok we are sending your FeedBack");
                        mSP.stopListening();
                        mSP.cancel();
                        sent = true;
                    }
                    else {
                        speeakVoice(mytring + "yes or no" );
                        String a[]= mytring.split(" ");
                        time =(1.0/a.length);
                        Toast.makeText(GoogleAccount.this,time.toString(),Toast.LENGTH_LONG).show();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSP.startListening(intent);
                        }
                    },2000);



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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSP.startListening(intent);
            }
        },5000);

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
