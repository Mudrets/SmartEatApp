package com.example.smarteat.vh

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R

class RecipeDelimiterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val header: TextView = itemView.findViewById(R.id.recipe_delimiter__header_text)
    val leftLine: View = itemView.findViewById(R.id.recipe_delimiter__left_line)
    val rightLine: View = itemView.findViewById(R.id.recipe_delimiter__right_line)

    fun setVisibility(visibility: Int) {
        header.visibility = visibility
        leftLine.visibility = visibility
        rightLine.visibility = visibility
    }
}