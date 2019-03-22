package com.example.quranichelper

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

class feedBack : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        setSupportActionBar(findViewById(R.id.my_toolbar))
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
    }
}
