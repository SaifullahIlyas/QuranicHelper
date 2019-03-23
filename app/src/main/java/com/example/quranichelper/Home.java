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
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Home extends AppCompatActivity {
    Animation animation;
    ImageView mic,speeker;
    Intent intent;
    TextToSpeech tts;
    SpeechRecognizer mSP;
    String spkrOutput;
    String menuSpkr;
    Intent NavigationINtent;
    Boolean flag=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        menuSpkr += "Plese Select one option from the Menu. Menu is ";
        menuSpkr += "1. Listen  2. Favorite list   3. Downloads   4.Last LIsten  5. FeedBack  6.Help";
          animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
          speeker = findViewById(R.id.speaker);
          speeker.startAnimation(animation);
          intilaizeEngine();

    }

protected void MenuOPtion(View view)
{
    //Intent intent = new Intent( this,feedbackJ.class);
   // startActivity(intent);
    setInetent();
    mSP.startListening(intent);
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
            Toast.makeText(Home.this,"Speach start",Toast.LENGTH_LONG).show();
            }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {
                   // Toast.makeText(Home.this,"This is End",Toast.LENGTH_LONG).show();

                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onError(int error) {
                    //Toast.makeText(Home.this,"Error Occur",Toast.LENGTH_LONG).show();
                    if (error==6)
                    speeakVoice("please Select the Right option");
                    try {
                        mSP.startListening(intent);
                    }
                  catch (Exception e)
                  {
                      Toast.makeText(Home.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                  }

                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if(result!=null) {
                        String result1 =result.get(0).toString();
                        String result2 = result.get(1).toString();
                        processResult(result1,result2);
                    }
                    else
                        speeakVoice("result is null");

                    mSP.startListening(intent);
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                private void processResult(String mytringRes1,String mytringRes2)//code to handle commands
                {
                    String myString;
                    if(mytringRes1.length()>10 && mytringRes1.contains(""))
                    {
                        myString = mytringRes2;
                    }
                    else {
                        myString = mytringRes1;
                    }
                    try {
                        {
                            if ((myString.contains("five")|| myString.contains("5"))&&myString.length()<10) {
                                NavigationINtent = new Intent(Home.this,feedbackJ.class) ;
                                startActivity(NavigationINtent);
                                flag= true;
                            }

                        }
                    }
                   catch (Exception e)
                   {
                       Toast.makeText(Home.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                   }
                }



                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPartialResults(Bundle partialResults) {
                    /*ArrayList<String> list = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                      processResult(list.get(0).toString(),list.get(1).toString());*/
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
         intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_RESULTS,2);




    }

    @Override
    protected void onResume() {
        super.onResume();
        intilaizeEngine();
        maintainSPeakerSpeed(1.0,0.5);
        getSpeachRecognizer();
        setInetent();
        flag = false;
        mSP.startListening(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        tts.stop();
        tts.shutdown();
        mSP.stopListening();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.stop();
        tts.shutdown();
        mSP.stopListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSP.stopListening();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mSP.startListening(intent);
    }
}
