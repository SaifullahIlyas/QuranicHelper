package com.example.quranichelper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class SurahList extends AppCompatActivity implements View.OnLongClickListener {
    ListView surahList;
    FirebaseFirestore fs;
    datalistdetail datalistdetail;
    ListView view;
    private  SearchView searchView;
    customSurahViewAdapter adapter;
    private static final String ACTION_VOICE_SEARCH = "com.google.android.gms.actions.SEARCH_ACTION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_list);
        surahList = findViewById(R.id.surahListView);
        setupSurahDictionary();
        handleIntent(getIntent());
      //  handleVoiceSearch(getIntent());
searchView = findViewById(R.id.search);
        setSupportActionBar((Toolbar) findViewById(R.id.searchmenubar));
        final ArrayList<datalistdetail> listname =new ArrayList<>();

        try {
            fs = FirebaseFirestore.getInstance();
            Task<QuerySnapshot> refrence = fs.collection("parah30").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> listss = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot val:queryDocumentSnapshots.getDocuments()) {
                        datalistdetail = new datalistdetail(val.getId().toString(),R.mipmap.mp3ic,R.mipmap.play);
                        listname.add(datalistdetail);
                        Log.d("DocumentId", val.getId().toString());
                    }


                    customSurahViewAdapter adapter = new customSurahViewAdapter(SurahList.this,listname);
                    ListView view = findViewById(R.id.surahListView);
                    view.setAdapter(adapter);

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
        }
        catch (Exception e)
        {
            Log.d("DocumentId", e.getLocalizedMessage());
        }


        findViewById(R.id.surahLishLayout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("LOng","Long click implemented");
                return false;
            }
        });
        view =  findViewById(R.id.surahListView);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        signIN();///sign in to uset
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle("search");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
    @Override
    public boolean onLongClick(View v) {

        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(getIntent());
        handleVoiceSearch(getIntent());
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this,query,Toast.LENGTH_LONG).show();
            searchView.setQuery(query,false);

        }
    }
    private void handleVoiceSearch(Intent intent) {
        if (intent != null && ACTION_VOICE_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this,query,Toast.LENGTH_LONG).show();
        }
    }
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
}
