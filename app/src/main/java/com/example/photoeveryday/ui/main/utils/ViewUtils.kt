package com.example.photoeveryday.ui.main.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

const val SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES"
const val THEME_PREFERENCES = "THEME_PREFERENCES"
const val KEY_DEFAULT_THEME = "DEFAULT_THEME"
const val KEY_RED_ROSE_THEME = "RED_ROSE_THEME"
const val CHECKED_CHIP_ON_MAIN_FRAGMENT_PREFERENCES = "CHECKED_CHIP_ON_MAIN_FRAGMENT_PREFERENCES"
const val KEY_TODAY_CHIP = "TODAY_CHIP"
const val KEY_YESTERDAY_CHIP = "YESTERDAY_CHIP"
const val KEY_TWO_DAYS_AGO_CHIP = "TWO_DAYS_AGO_CHIP"

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}