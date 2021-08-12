package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Achievement
import com.example.smarteat.vh.AchievementViewHolder

/**
 * Адаптер для достижений
 */
class AchievementAdapter(
    private val achievements: ArrayList<Achievement> = ArrayList() //Достижения, которые нужно отобразить
): RecyclerView.Adapter<AchievementViewHolder>() {
    override fun getItemViewType(position: Int): Int = R.layout.achievement_row

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return AchievementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = achievements[position]
        holder.name.text = achievement.name
        holder.description.text = achievement.description
        holder.image.setImageDrawable(achievement.image)
    }

    override fun getItemCount(): Int = achievements.size
}