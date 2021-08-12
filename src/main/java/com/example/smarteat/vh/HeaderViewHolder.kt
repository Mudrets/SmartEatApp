package com.example.smarteat.vh

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R

class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val header: TextView = itemView.findViewById(R.id.header_row__header)
}