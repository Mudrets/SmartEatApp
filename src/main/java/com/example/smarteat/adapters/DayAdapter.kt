package com.example.smarteat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Day

class DayAdapter(

    private val days: ArrayList<Day>
) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val caloriesTextView: TextView = itemView.findViewById(R.id.day_row__num_calories)
        val dayTextView: TextView = itemView.findViewById(R.id.day_row__num_of_day)
        val eatingRecyclerView: RecyclerView = itemView.findViewById(R.id.day_row__eating_recycler)
        val arrowIcon: ImageView = itemView.findViewById(R.id.day_row__main_arrow)
        val view: View = itemView

        init {
            val clickListener = View.OnClickListener { _ ->
                val day = days[adapterPosition]
                day.isExpanded = !day.isExpanded
                notifyItemChanged(adapterPosition)
            }
            dayTextView.setOnClickListener(clickListener)
            caloriesTextView.setOnClickListener(clickListener)
            arrowIcon.setOnClickListener(clickListener)
            eatingRecyclerView.layoutManager = object : LinearLayoutManager(itemView.context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.day_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]

        val caloriesString = "$position ККАЛ"
        holder.caloriesTextView.text = caloriesString
        val numOfDay = position + 1
        val dayText = "ДЕНЬ $numOfDay"
        holder.dayTextView.text = dayText

        holder.eatingRecyclerView.adapter = EatingAdapter(day.eating)

        val isExpanded = day.isExpanded
        holder.arrowIcon.rotationX = if (isExpanded) 180F else 0F
        holder.eatingRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = days.size
}