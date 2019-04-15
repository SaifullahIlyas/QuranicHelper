package com.example.quranichelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout linearLayout;
    String menuSpkr;
    Intent NavigationINtent;
    Boolean flag=false;
    private  GestureDetector myGestureDetc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        linearLayout  = findViewById(R.id.mainHomeLayout);

        menuSpkr += "You are on Home Screen";
          animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        findViewById(R.id.listenCard).setOnClickListener(new DoubleClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSingleClick(View v) {
                speeakVoice("Double Tap to listen");

            }

            @Override
            public void onDoubleClick(View v) {
                Intent intent = new Intent( Home.this,SurahList.class);
                startActivity(intent);

            }
        });
        findViewById(R.id.downloadsCard).setOnClickListener(new DoubleClickListener() {

            @Override
            public void onSingleClick(View v) {
                speeakVoice("Double Tap for Rate Application");

            }

            @Override
            public void onDoubleClick(View v) {
                Intent intent = new Intent( Home.this,Rate.class);
                startActivity(intent);

            }
        });
        findViewById(R.id.lastlistenCard).setOnClickListener(new DoubleClickListener() {

            @Override
            public void onSingleClick(View v) {
                speeakVoice("Double Tap for Last listen");

            }

            @Override
            public void onDoubleClick(View v) {
                Intent intent = new Intent( Home.this,LastListen.class);
                startActivity(intent);

            }
        });
        findViewById(R.id.feedbackCard).setOnClickListener(new DoubleClickListener() {

            @Override
            public void onSingleClick(View v) {
                speeakVoice("Double Tap For feedback");

            }

            @Override
            public void onDoubleClick(View v) {
                Intent intent = new Intent( Home.this,feedbackJ.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.helpCard).setOnClickListener(new DoubleClickListener() {

            @Override
            public void onSingleClick(View v) {
                speeakVoice("Double Tap for Help");
            }

            @Override
            public void onDoubleClick(View v) {

            }
        });


    }
    void calculatedistance(float a ,float b) {
        ArrayList<View> allButtons;
        allButtons = ((LinearLayout) findViewById(R.id.mainHomeLayout)).getTouchables();
        Toast.makeText(Home.this,allButtons.size(),Toast.LENGTH_LONG).show();
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
                if(  buffer.length>1)
                    Toast.makeText(Home.this,"more than one voice detecd",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onEndOfSpeech() {
                   // Toast.makeText(Home.this,"This is End",Toast.LENGTH_LONG).show();

                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onError(int error) {
                    //Toast.makeText(Home.this,"Error Occur",Toast.LENGTH_LONG).show();
                    if (error==6) {
                        speeakVoice("please Select the Right option");
                        try {
                            mSP.startListening(intent);
                        } catch (Exception e) {
                            Toast.makeText(Home.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
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
                    myString = mytringRes1;
                    myString.toLowerCase();
                        try {
                        {
                            if ((myString.contains("five")|| myString.contains("5")||myString.contains("feedback"))&&myString.length()<10) {
                                NavigationINtent = new Intent(Home.this,feedbackJ.class) ;
                                startActivity(NavigationINtent);
                                flag= true;
                            }
                            if((myString.contains("1")|| myString.contains("one")||myString.contains("listen"))&&myString.length()<10)
                            {
                                NavigationINtent = new Intent(Home.this,Listen.class) ;
                                startActivity(NavigationINtent);
                                flag= true;
                            }
                            if((myString.contains("two")|| myString.contains("2")||myString.contains("favourite list"))&&myString.length()<10)
                            {
                                NavigationINtent = new Intent(Home.this,FavoriteList.class) ;
                                startActivity(NavigationINtent);
                                flag= true;
                            }
                            if((myString.contains("three")|| myString.contains("3")||myString.contains("downloads"))&&myString.length()<10)
                            {
                                NavigationINtent = new Intent(Home.this,Downloads.class) ;
                                startActivity(NavigationINtent);
                                flag= true;

                            }
                             if((myString.contains("four")|| myString.contains("4")||myString.contains("last listen"))&&myString.length()<10)
                            {
                                NavigationINtent = new Intent(Home.this,LastListen.class) ;
                                startActivity(NavigationINtent);
                                flag= true;
                            }


                        }
                    }
                   catch (Exception e)
                   {
                       Toast.makeText(Home.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                   }
                    mSP.startListening(intent);
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
        intent.putExtra(RecognizerIntent.EXTRA_RESULTS,1);




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
        mSP.cancel();
        mSP.destroy();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.stop();
        tts.shutdown();
        mSP.stopListening();
        mSP.cancel();
        mSP.destroy();
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


}
