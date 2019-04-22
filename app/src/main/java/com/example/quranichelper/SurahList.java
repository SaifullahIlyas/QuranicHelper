package com.example.quranichelper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SurahList extends AppCompatActivity {
    SpeechRecognizer mSP;
    TextToSpeech tts;
    ArrayList<datalistdetail> surahArrayList;
     ArrayList<datalistdetail> listname;
    Intent  intent;
    Map<String,String[]> mykeyWordDictionary;
String[] suratMoedl;
    ListView surahList;
    FirebaseFirestore fs;
    datalistdetail datalistdetail;
    ListView view;
    int confidence = 0;
    private  SearchView searchView;
    customSurahViewAdapter customadapter;
    Switch surahSwith;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        suratMoedl = getResources().getStringArray(R.array.suraharray);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_list);
        surahList = findViewById(R.id.surahListView);

        setupSurahDictionary();
surahArrayList =  new ArrayList<>()  ;    //  handleVoiceSearch(getIntent())


searchView = findViewById(R.id.search);
        setSupportActionBar((Toolbar) findViewById(R.id.searchmenubar));
        surahSwith =  findViewById(R.id.surahSwitch);
        surahSwith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked())
                {
                    customadapter.resetAdapter(surahArrayList);
                    customadapter.notifyDataSetChanged();
                }
                else {
                    customadapter.resetAdapter(listname);
                    customadapter.notifyDataSetChanged();
                }

            }
        });

        listname =new ArrayList<>();

        try {
            fs = FirebaseFirestore.getInstance();
            Task<QuerySnapshot> refrence = fs.collection("parah").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> listss = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot val:queryDocumentSnapshots.getDocuments()) {
                        datalistdetail = new datalistdetail(val.getId().toString(),R.mipmap.ic_quran,R.mipmap.play);
                        listname.add(datalistdetail);
                        Log.d("DocumentId", val.getId().toString());
                    }


                    customadapter = new customSurahViewAdapter(SurahList.this,listname);
                    ListView view = findViewById(R.id.surahListView);
                    view.setAdapter(customadapter);

                }
            }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    /*QuerySnapshot result = task.getResult();
                    for(DocumentSnapshot val:result.getDocuments())
                    {
                        datalistdetail = new datalistdetail(val.getId().toString(),R.mipmap.mp3ic,R.mipmap.play);
                        listname.add(datalistdetail);
                       // Log.d("DocumentId", val.getId().toString());


                    }
                    for(int i=0;i<listname.size();i++)
                    { Log.d("djsjasdnsdnmnmdnmnd",String.valueOf(listname.size()));
                        com.example.quranichelper.datalistdetail data ;
                        data = listname.get(i);
                        Log.d("value",data.getName());
                    }
*/
                }
            });
            fs.collection("surah").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> listss = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot val:queryDocumentSnapshots.getDocuments()) {
                        datalistdetail = new datalistdetail(val.getId().toString(),R.mipmap.ic_quran_round,R.mipmap.play);
                        surahArrayList.add(datalistdetail);
                        Log.d("DocumentId", val.getId().toString());
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        catch (Exception e)
        {
            Log.d("DocumentId", e.getLocalizedMessage());
        }

        view =  findViewById(R.id.surahListView);
        view.setLongClickable(true);
        view.setClickable(true);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirebaseFirestore Fs  = FirebaseFirestore.getInstance();

                com.example.quranichelper.datalistdetail data;

                long myid = view.getId();
                if(myid==R.id.listplay) {
                    Intent intent = new Intent(SurahList.this,Listen.class);
                    if(surahSwith.isChecked()) {
                        data =  surahArrayList.get(position);
                        intent.putExtra("surahname", data.getName().toString());
                        intent.putExtra("switch","dgdgsgsg");
                    }
                    else {
                        data =  listname.get(position);
                        intent.putExtra("surahname", data.getName().toString());
                        intent.putExtra("switch","");

                    }
                    startActivity(intent);
                }
                if(surahSwith.isChecked()) {
                    com.example.quranichelper.datalistdetail datalistdetail =surahArrayList.get(position);
                    speeakVoice("this is Surah " + datalistdetail.getName());
                }
                else {
                    com.example.quranichelper.datalistdetail datalistdetail =listname.get(position);
                    speeakVoice("this is para " + datalistdetail.getName());
                }

            }
        });
        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SurahList.this,String.valueOf(id),Toast.LENGTH_LONG).show();
                mSP.startListening(intent);
                return  true;
            }
        });



        signIN();///sign in to uset
    }
    private View.OnClickListener myButtonClickListener = new View.OnClickListener() {

        @Override

        public void onClick(View v) {

            View parentRow = (View) v.getParent();

            ListView listView = (ListView) parentRow.getParent();

            final int position = listView.getPositionForView(parentRow);

        }

    };


    void setupSurahDictionary()
{
    String[] array = getResources().getStringArray(R.array.suraharray);
    for(String s:array)
        Log.d("smsmms",s);
}
private void setOnItemListListner()
{
   view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           Toast.makeText(SurahList.this,String.valueOf(position),Toast.LENGTH_LONG).show();
           view.getParent();
       }
   });
}
void signIN()
{
    FirebaseAuth Auth  = FirebaseAuth.getInstance();
    if(Auth==null)
    {
        Log.d("userAuth","NouserFound");
    }
    else
    {
        Auth.signInWithEmailAndPassword("saifullahilyas907@gmail.com","miang367").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("userAuth","Successfully signIn");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("userAuth",e.getLocalizedMessage());
            }
        });
    }
}
//speach recognizer code
public void setInetent()//set intent
{
    intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_RESULTS,1);




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
                Toast.makeText(SurahList.this,"Speach start",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                if(  buffer.length>1)
                    Toast.makeText(SurahList.this,"more than one voice detecd",Toast.LENGTH_LONG).show();
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
                   // speeakVoice("please Select the Right option");

                    try {
                        mSP.startListening(intent);
                    } catch (Exception e) {
                        Toast.makeText(SurahList.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResults(Bundle results) {
                try {
                    ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if(result!=null) {
                        String result1 =result.get(0).toString();
                        processResult(result1);
                        Log.d("Recognizer input",result1 );
                    }
                }
                catch (Exception e)
                {
                    Log.d("Message",e.getLocalizedMessage());
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            private void processResult(String mytringRes1)//code to handle commands
            {
                String outputval = "";
                String surahNAmeBuilder = "";
                String[] surahModel = new String[]{"Surah", "Sura", "Surat", "Subura"};
                String[] playModel = new String[]{"Play", "lay", "playyy"};
                String[] paraMOdel = new String[]{"para", "parah", "Tara", ""};

                String[] myString = mytringRes1.split(" ");

                String s = getSurahConfidence(mytringRes1);
                if (s == "parah30") {
                    Intent intent = new Intent(SurahList.this, Listen.class);
                    intent.putExtra("surahname", "parah30");
                    startActivity(intent);
                } else if (s == "Type1") {
                   String ss = mytringRes1.replaceAll("\\D+","");
                   Log.d("Surah",ss);
                }

            }


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

}

String getSurahConfidence(String name) {
        String command = "";
        String commandType1 = "play para 30";
        String commandType2 = "play para 30 Surah 30";
        String commandType3 = "play para 30 surah 30 Ayat 30 ";
        int length = name.length();
        if(length <=commandType1.length()+2 && name.contains("play")||name.contains("lay"))
        {
            command ="parah30";
        }
        else if(length<=commandType2.length()+2&& name.contains("play")||name.contains("lay")&&(name.contains("Surah")||name.contains("sura")))
        {
            command =  "type2";
        }
        else  if(length<=commandType3.length()+2&& (name.contains("play")||name.contains("lay")))
        {
            command = "type3";
        }
        return  command;
}

    @Override
    protected void onResume() {
        super.onResume();
        getSpeachRecognizer();
        setInetent();
        intilaizeEngine();
        maintainSPeakerSpeed(0.7,0.7);
        //mSP.startListening(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSP.stopListening();
        mSP.cancel();;
        mSP.destroy();
    }
void  createDictionary()
{
    mykeyWordDictionary.put("Al-Fajr",new String[]{"Alphonsa"});
    mykeyWordDictionary.put("Al-fil",new String[]{"I'll feel"});
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
                    speeakVoice("you are on surah list screen");
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
}
