package com.example.quranichelper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val animation2 = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.blink
        )
        val animation = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.blink
        )
        val mic = findViewById(R.id.mic) as? ImageView
        mic!!.startAnimation(animation)
        val mic1 = findViewById(R.id.mic) as? ImageView

    }
    fun MenuOPtion(view:View)
    {
        try {
            val intent = Intent(this, feedBack::class.java)
            startActivity(intent)
        }
        catch (e:Exception)
        {
            Toast.makeText(this,e.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}
