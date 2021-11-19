package com.example.photoeveryday.ui.main.view.picture

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.photoeveryday.R
import com.example.photoeveryday.ui.main.view.recycler.RecyclerActivity
import com.example.photoeveryday.ui.main.view.animations.AnimationsActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_navigation_layout.*

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_navigation_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_one -> {
                    activity?.let {
                        startActivity(Intent(it, AnimationsActivity::class.java))
                    }
                }

                R.id.navigation_two -> {
                    activity?.let {
                        startActivity(Intent(it, RecyclerActivity::class.java))
                    }
                }
            }
            true
        }
    }
}