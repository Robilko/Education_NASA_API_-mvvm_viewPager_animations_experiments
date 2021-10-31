package com.example.photoeveryday.ui.main.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

const val SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES"
const val THEME_PREFERENCES = "THEME_PREFERENCES"
const val KEY_DEFAULT_THEME = "DEFAULT_THEME"
const val KEY_COSMIC_THEME = "COSMIC_THEME"
const val CHIP_THEME_SELECTED = "CHIP_THEME_SELECTED"
const val APP_FONT_FAMILY = "APP_FONT_FAMILY"

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}