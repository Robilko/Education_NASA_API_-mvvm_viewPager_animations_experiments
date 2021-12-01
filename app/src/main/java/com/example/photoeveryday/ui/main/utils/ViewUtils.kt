package com.example.photoeveryday.ui.main.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

const val SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES"
const val THEME_PREFERENCES = "THEME_PREFERENCES"
const val KEY_DEFAULT_THEME = "DEFAULT_THEME"
const val KEY_RED_ROSE_THEME = "RED_ROSE_THEME"
const val TYPE_EARTH = 0
const val TYPE_MARS = 1
const val TYPE_NOTE = 2
const val TYPE_HEADER = 3
const val EDIT_NOTE_FRAGMENT = "EDIT_NOTE_FRAGMENT"
const val NOTE_LIST_FRAGMENT = "NOTE_LIST_FRAGMENT"

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}