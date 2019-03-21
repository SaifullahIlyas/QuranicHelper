package com.example.quranichelper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import android.widget.ImageView


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

    }
}
