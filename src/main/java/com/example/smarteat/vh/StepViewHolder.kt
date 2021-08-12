package com.example.smarteat.vh

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R

class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val numOfStep: TextView = itemView.findViewById(R.id.cook_step_row__step_num)
    val stepContent: TextView = itemView.findViewById(R.id.cook_step_row__step_content)
}