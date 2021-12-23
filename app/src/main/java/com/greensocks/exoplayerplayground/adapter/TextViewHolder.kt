package com.greensocks.exoplayerplayground.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greensocks.exoplayerplayground.model.TextModel

class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view as TextView

    fun bind(model: TextModel) {
        textView.text = model.text
    }
}
