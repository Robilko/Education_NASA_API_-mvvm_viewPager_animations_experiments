package com.example.photoeveryday.ui.main.view.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeveryday.ui.main.model.Data

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract  fun bind(data: Pair<Data, Boolean>)
}