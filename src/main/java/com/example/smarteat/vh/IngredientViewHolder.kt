package com.example.smarteat.vh

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Eating
import com.example.smarteat.ui.dialogs.DialogOneButton
import com.example.smarteat.ui.fragments.FragmentWithPlan

class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTextView: TextView = itemView.findViewById(R.id.ingredient_row__ingredient_point)
    val weightTextView: TextView = itemView.findViewById(R.id.ingredient_row__ingredient_weight)
    val layout: ViewGroup = itemView.findViewById(R.id.ingredient_row__layout)

    constructor(itemView: View, parentFragment: FragmentWithPlan, eating: Eating): this(itemView) {
        val onClickListener = View.OnClickListener {
            val recipes = parentFragment.user.activePlan!!.recipes[eating.recipeNum / 100 - 1]
            val recipePositions = ArrayList<Pair<Int, Int>>()
            for (recipe in eating.recipes) {
                val index = recipes.indexOf(recipe)
                recipePositions.add(Pair(eating.recipeNum / 100 - 1, index))
            }
            if (recipePositions.isNotEmpty())
                parentFragment.parentActivity.showRecipe(recipePositions)
            else {
                val dialog = DialogOneButton(
                    "Нет рецепта",
                    "К сожалению, для выбранного вами блюда нет рецепта, так как его приготовление является достаточно простым и не требует описания.",
                    "Продолжить"
                )
                parentFragment.parentActivity.showDialog(dialog)
            }
        }
        nameTextView.setOnClickListener(onClickListener)
        layout.setOnClickListener(onClickListener)
        weightTextView.setOnClickListener(onClickListener)
    }
}