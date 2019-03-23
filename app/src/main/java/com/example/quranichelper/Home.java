package com.example.quranichelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Home extends AppCompatActivity {
    Animation animation;
    ImageView mic,speeker;
    TextToSpeech tts;
    SpeechRecognizer mSP;
    String spkrOutput;
    String menuSpkr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        menuSpkr += "Plese Select one option from the Menu. Menu is ";
        menuSpkr += "1.Listen  2.Favorite list   3.Downloads   4.Last LIsten  5.FeedBack  6.Help";
          animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
          speeker = findViewById(R.id.speaker);
          speeker.startAnimation(animation);
          intilaizeEngine();
          maintainSPeakerSpeed(1.0,0.5);
          getSpeachRecognizer();
          setInetent();
    }

protected void MenuOPtion(View view)
{
    Intent intent = new Intent( this,feedbackJ.class);
    startActivity(intent);
}
public void perepereMenu()
{

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
                    speeakVoice(menuSpkr);
                }

            }
        });
    }
    public  void maintainSPeakerSpeed(double pitch , double spRate)//set Speed of the Engine .
    {
        tts.setPitch((float)pitch);
        tts.setSpeechRate((float)spRate);

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

                @Override
                public void onError(int error) {

                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if(result!=null)
                        processResult(result.get(0).toString());
                    else
                        speeakVoice("result is null");
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                private void processResult(String mytring)//code to handle commands
                {

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
 public void   intilizeSpeekerOutput(String input){
        spkrOutput += input;
        spkrOutput+="    ";

 }

    public void setInetent()//set intent
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_RESULTS,1);
        mSP.startListening(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        tts.stop();
        tts.shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.stop();
        tts.shutdown();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        intilaizeEngine();
    }
}
