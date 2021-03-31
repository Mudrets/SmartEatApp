package com.example.smarteat.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Eating

class EatingAdapter(
    private val eating: ArrayList<Eating>
) : RecyclerView.Adapter<EatingAdapter.EatingViewHolder>() {

    inner class EatingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.eating_row__eating_name)
        val line : View = itemView.findViewById(R.id.eating_row__line)
        val arrowIcon : ImageView = itemView.findViewById(R.id.eating_row__main_arrow)
        val expandedProperties : CardView = itemView.findViewById(R.id.eating_row__expanded_properties)
        val ingredients : RecyclerView = itemView.findViewById(R.id.eating_row__ingredients_list)
        val view: View = itemView

        init {
            val clickListener = View.OnClickListener { _ ->
                val e = eating[adapterPosition]
                e.isExpanded = !e.isExpanded
                notifyItemChanged(adapterPosition)
            }
            arrowIcon.setOnClickListener(clickListener)
            nameTextView.setOnClickListener(clickListener)
            ingredients.layoutManager = object : LinearLayoutManager(itemView.context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    private fun translate(string: String): String =
        when (string) {
            "breakfast" -> "Завтрак"
            "dinner" -> "Обед"
            "snack1" -> "Перекус 1"
            "snack2" -> "Перекус 2"
            "snack3" -> "Перекус 3"
            "supper" -> "Ужин"
            else -> ""
        }

    override fun getItemViewType(position: Int): Int {
        return R.layout.eating_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EatingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        return EatingViewHolder(view)
    }

    override fun onBindViewHolder(holder: EatingViewHolder, position: Int) {
        val currEating = eating[position]
        holder.nameTextView.text = translate(currEating.name)
        holder.ingredients.adapter = IngredientsAdapter(currEating.ingredients)

        if (position == eating.size - 1)
            holder.line.visibility = View.GONE

        val isExpanded = currEating.isExpanded
        holder.arrowIcon.rotationX = if (isExpanded) 180F else 0F
        holder.expandedProperties.visibility = if (isExpanded) View.VISIBLE else View.GONE

    }

    override fun getItemCount(): Int = eating.size
}