package com.example.smarteat.vh

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

abstract class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract val productName: TextView
    abstract val weight: TextView
    abstract val productCard: CardView
    abstract val radioButton: RadioButton
}