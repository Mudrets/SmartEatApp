package com.example.smarteat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Ingredient

class IngredientsAdapter(
    private val ingredients: ArrayList<Ingredient>
) : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.ingredient_row__ingredient_point)
        val weightTextView: TextView = itemView.findViewById(R.id.ingredient_row__ingredient_weight)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.ingredients_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return IngredientViewHolder(view)
    }

    override fun getItemCount(): Int = ingredients.size

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        val name = ingredient.name
        val weight = ingredient.weight
        val nameStr = "- $name"
        val weightStr = "$weight гр."
        holder.nameTextView.text = nameStr
        holder.weightTextView.text = weightStr
    }
}