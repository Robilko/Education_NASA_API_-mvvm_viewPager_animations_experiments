package com.example.photoeveryday.ui.main.view.api

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.photoeveryday.R
import com.example.photoeveryday.ui.main.utils.KEY_RED_ROSE_THEME
import com.example.photoeveryday.ui.main.utils.KEY_DEFAULT_THEME
import com.example.photoeveryday.ui.main.utils.SETTINGS_PREFERENCES
import com.example.photoeveryday.ui.main.utils.THEME_PREFERENCES
import com.example.photoeveryday.ui.main.view.api.ViewPagerAdapter
import kotlinx.android.synthetic.main.api_activity.*

class ApiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme()
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

    private fun setTheme() {
        val theme = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE).getString(
            THEME_PREFERENCES, KEY_DEFAULT_THEME
        )

        if (theme.equals(KEY_RED_ROSE_THEME)) {
            setTheme(R.style.RedRoseTheme)
        } else {
            setTheme(R.style.DefaultTheme)
        }
    }
}