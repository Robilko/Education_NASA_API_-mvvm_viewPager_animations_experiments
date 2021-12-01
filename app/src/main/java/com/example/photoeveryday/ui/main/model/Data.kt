package com.example.photoeveryday.ui.main.model

import java.io.Serializable
import java.util.*

data class Data(val id: String, var someText: String = "Text", var someDescription: String? = "Description", val viewType: Int = 2) : Serializable {

    companion object {
        fun generateNewId(): String {
            return UUID.randomUUID().toString()
        }
    }
}
