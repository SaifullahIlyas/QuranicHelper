package com.example.quranichelper

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import  speechRecognitionOperations
import java.util.*

class feedBack : AppCompatActivity(),TextToSpeech.OnInitListener{
    public lateinit var ttSystem:TextToSpeech
    public lateinit var  mSpeechRecognizer:SpeechRecognizer
    public lateinit var  mSpeechRecognizerIntent:Intent
    override fun onInit(status: Int) {

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        setSupportActionBar(findViewById(R.id.my_toolbar))
      ttSystem = TextToSpeech(this,this)
        ttSystem.speak("Please Record your feedBack",TextToSpeech.QUEUE_FLUSH, null,"")

        mSpeechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {

            }

            override fun onBeginningOfSpeech() {

            }

            override fun onRmsChanged(v: Float) {}

            override fun onBufferReceived(bytes: ByteArray) {}

            override fun onEndOfSpeech() {}

            override fun onError(i: Int) {}

            override fun onResults(bundle: Bundle) {
                val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)//getting all the matches
                //displaying the first match
                if (matches != null) {
                    val input = findViewById<EditText>(R.id.messageFeed)
                    input.setText(matches[0].toString())
                }
            }

            override fun onPartialResults(bundle: Bundle) {}

            override fun onEvent(i: Int, bundle: Bundle) {}
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun messageSentListner(view:View)
    {
        message()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun message() {
        val input = findViewById<EditText>(R.id.messageFeed)
        val refrence = FirebaseFirestore.getInstance()
        val date = LocalDateTime.now()
        val map = HashMap<String,Any>()
        map["message"] = input.text.toString()
        map["time"] = date.toString()


        if (input.text != null){
            try {
                refrence.collection("Feedback").document("1").set(map).addOnSuccessListener {

                }.addOnFailureListener{e->
                    Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
                }
            }
            catch (e:Exception)
            {
                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }
        }
        else{
            //here is the code for Speaker
        }
    }fun startRecognizer()
    {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        mSpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        mSpeechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        mSpeechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        );
    }
    fun stopRecognizer()
    {
        mSpeechRecognizer.destroy()
    }
}
