package com.example.smarteat.vh

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R

class AchievementViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.achievement_row__achievement_name)
    val description: TextView = itemView.findViewById(R.id.achievement_row__achievement_description)
    val image: ImageView = itemView.findViewById(R.id.achievement_row__achievement_image)
}