package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Eating
import com.example.smarteat.models.Ingredient
import com.example.smarteat.ui.dialogs.DialogOneButton
import com.example.smarteat.ui.fragments.FragmentWithPlan
import com.example.smarteat.vh.IngredientViewHolder

class IngredientsAdapter(
    private val ingredients: ArrayList<Ingredient>,
    private val parentFragment: FragmentWithPlan,
    private val eating: Eating
) : RecyclerView.Adapter<IngredientViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return R.layout.ingredients_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return IngredientViewHolder(view, parentFragment, eating)
    }

    override fun getItemCount(): Int = ingredients.size

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        val name = ingredient.name
        val nameStr = "- $name"
        holder.nameTextView.text = nameStr
        holder.weightTextView.text = "${ingredient.weight} г."
    }
}