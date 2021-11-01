package com.example.photoeveryday.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.photoeveryday.R

class ApiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
    }
}