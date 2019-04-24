package com.example.quranichelper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
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

import java.util.*;

public class SurahList extends AppCompatActivity {
    SpeechRecognizer mSP;
    TextToSpeech tts;
    Map<String,String[]> parahDic;
    Map<String,String[]> surahDic;
    ArrayList<datalistdetail> surahArrayList;
    ArrayList<datalistdetail> listname;
    Intent  intent;
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
        parahDic = new HashMap<>();
        surahDic  = new HashMap<>();
        intilizeParahDictionary();//intilize parah dictionary
        intilizeSurahDictionary();// intilize sura dictionary
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
                speeakVoice("speak parah or surah number");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSP.startListening(intent);
                    }
                },2000);

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
                    String parahResult;
                    String outputval = "";
                    String surahNAmeBuilder = "";
                    String[] surahModel = new String[]{"Surah", "Sura", "Surat", "Subura"};
                    String[] playModel = new String[]{"Play", "lay", "playyy"};
                    String[] paraMOdel = new String[]{"para", "parah", "Tara"};

                    String[] myString = mytringRes1.split(" ");

                    if(myString.length<=3)//first check for command
                    { Log.d("String ",myString[0]);
                        Log.d("String ",myString[1]);
                        Log.d("String ",myString[2]);

                        if(stringCompare(myString[0].toLowerCase().trim(),playModel[0].toLowerCase().trim())==0 || stringCompare(myString[0].toLowerCase().trim(),playModel[1].toLowerCase().trim())==0 || stringCompare(myString[0].toLowerCase().trim(),playModel[2].toLowerCase().trim())==0)
                        {
                            if(stringCompare(myString[1].toLowerCase().trim(),paraMOdel[0].toLowerCase().trim())==0 || stringCompare(myString[1].toLowerCase().trim(),paraMOdel[1].toLowerCase().trim())==0 || stringCompare(myString[1].toLowerCase().trim(),paraMOdel[2].toLowerCase().trim())==0)
                            {
                                Log.d("in parah","yes");
                                parahResult =  findParahName(myString[2].trim());
                                intent.putExtra("surahname",parahResult.trim());
                                intent.putExtra("switch","dgdgsgsg");
                                startActivity(intent);
                            }
                            else if (stringCompare(myString[1].toLowerCase().trim(),surahModel[0].toLowerCase().trim())==0 || stringCompare(myString[1].toLowerCase().trim(),surahModel[1].toLowerCase().trim())==0 || stringCompare(myString[1].toLowerCase().trim(),surahModel[2].toLowerCase().trim())==0)
                            {
                                parahResult  = findSurahName(myString[2]);
                                intent.putExtra("surahname",parahResult.trim());
                                intent.putExtra("switch","dgdgsgsg");
                                startActivity(intent);
                            }


                        }
                        else
                            speeakVoice("make sure you command start with play");




                    }
                    else {
                        speeakVoice("make sure you are speaking the right command and verify the noice");
                    }

                }

                private String findSurahName(String  name)//find surah name that effected person want to play
                { String val = "Alif Lam Meem";

                    return val ;
                }

                private String findParahName(String name)//method to find the parah name that the affected person want to play
                {
                    String val = "Alif Lam Meem";
                    for (Map.Entry<String, String[]> entry : parahDic.entrySet()) {
                        String key = entry.getKey();
                        String [] arrayval =  entry.getValue();
                        for (String s : arrayval)
                        {
                            if(stringCompare(name.toLowerCase().trim(),s.toLowerCase().trim())==0)
                            {
                                val = key;
                                break;

                            }
                        }
                    }
                    return val;

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
    private  void intilizeParahDictionary()
    {
        parahDic .put("Alif Lam Meem",new String[]{"1","one"});
        parahDic .put("Sayaqool",new String[]{"2","two"});
        parahDic .put("Tilkal Rusull",new String[]{"3","three"});
        parahDic .put("Wal Mohsanat",new String[]{"4","four"});
        parahDic .put("La Yuhibbullah",new String[]{"5","five"});
        parahDic .put("Wa Iza Samiu",new String[]{"6","six"});
        parahDic .put("Wa Lau Annana",new String[]{"7","seven"});
        parahDic .put("Qalal Malao",new String[]{"8","eight"});
        parahDic .put("Wa Alamu",new String[]{"9","nine"});
        parahDic .put("Yatazeroon",new String[]{"10","ten"});
    }
    private  void  intilizeSurahDictionary()
    {
        surahDic .put("Al-Fatiha",new String[]{"1","One"});
        surahDic .put("Al-Baqara",new String[]{"2","two"});
        surahDic .put("Al-Imran",new String[]{"3","three"});
        surahDic .put("An-Nisa",new String[]{"4","four"});
        surahDic .put("Al-Maida",new String[]{"5","five"});
        surahDic .put("Al-Anam",new String[]{"6","six"});
        surahDic.put("Al-Araf",new String[]{"7","seven"});
        surahDic .put("Al-Anfal",new String[]{"8","eight"});
        surahDic .put("At-Tawba",new String[]{"9","nine"});
        surahDic .put("Yunus",new String[]{"10","ten"});
        surahDic .put("Hud",new String[]{"11","eleven"});
        surahDic.put("Yusuf",new String[]{"12","twelve"});
        surahDic .put("Ar-Rad",new String[]{"13","thirteen"});
        surahDic .put("Ibrahim",new String[]{"14","fourteen"});
        surahDic .put("Al-Hijr",new String[]{"15","fifteen"});

    }
    public static int stringCompare(String str1, String str2)
    {

        int l1 = str1.length();
        int l2 = str2.length();
        int lmin = Math.min(l1, l2);

        for (int i = 0; i < lmin; i++) {
            int str1_ch = (int)str1.charAt(i);
            int str2_ch = (int)str2.charAt(i);

            if (str1_ch != str2_ch) {
                return str1_ch - str2_ch;
            }
        }

        if (l1 != l2) {
            return l1 - l2;
        }

        else {
            return 0;
        }
    }
}
