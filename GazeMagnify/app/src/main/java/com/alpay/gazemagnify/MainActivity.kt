package com.alpay.gazemagnify

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgvw.setOnClickListener({listener -> startAnim()})
    }

    fun startAnim(){
        val aniSlide = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in)
        imgvw.startAnimation(aniSlide)
    }

}
