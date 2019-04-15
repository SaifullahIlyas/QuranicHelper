package com.example.quranichelper;

import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class NetworError extends AppCompatActivity {
    public TextToSpeech tts;
    TextView view1,view2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networ_error);
        view1 = findViewById(R.id.nointernetlable);
        view2 = findViewById(R.id.nointernetlable2);
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
                    speeakVoice(view1.getText().toString().replace('.',' ')+ " "+view2.getText().toString());
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
    public void  setSpeedrate(double d)
    {
        tts.setSpeechRate((float) d);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intilaizeEngine();
        setSpeedrate(0.7);

    }
}
