package com.example.photoeveryday.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
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
        setCustomTabs()
    }

    private fun setCustomTabs() {
        val layoutInflater = LayoutInflater.from(this)
        tab_layout.getTabAt(0)?.customView = layoutInflater.inflate(R.layout.activity_api_custom_tab_earth, null)
        tab_layout.getTabAt(1)?.customView = layoutInflater.inflate(R.layout.activity_api_custom_tab_mars, null)
        tab_layout.getTabAt(2)?.customView = layoutInflater.inflate(R.layout.activity_api_custom_tab_weather, null)
    }
}