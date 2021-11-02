package com.example.photoeveryday.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.photoeveryday.R
import kotlinx.android.synthetic.main.api_activity.*

class ApiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.api_activity)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_earth)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_mars)
        tab_layout.getTabAt(2)?.setIcon(R.drawable.ic_system)
    }
}