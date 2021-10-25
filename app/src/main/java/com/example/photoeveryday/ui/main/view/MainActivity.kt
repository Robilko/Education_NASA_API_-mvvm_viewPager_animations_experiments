package com.example.photoeveryday.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.photoeveryday.R
import com.example.photoeveryday.ui.main.view.main.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment())
                .commitNow()
        }
    }
}