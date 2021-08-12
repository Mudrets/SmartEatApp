package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Recipe
import com.example.smarteat.vh.StepViewHolder
import com.example.smarteat.vh.SubrecipeViewHolder

class StepAdapter(
    private val recipe: Recipe
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return if (recipe.subRecipes.size > 0) {
            if (position == 0 || position == recipe.stepsForCooking[0].size + 1)
                R.layout.subrecipe_name_row
            else
                R.layout.cook_step_row
        } else
            R.layout.cook_step_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.cook_step_row -> StepViewHolder(view)
            else -> SubrecipeViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = holder.itemViewType
        when (viewType) {
            R.layout.cook_step_row -> {
                val stepContent: String
                if (recipe.subRecipes.size == 0) {
                    stepContent = recipe.stepsForCooking[0][position]
                    (holder as StepViewHolder).numOfStep.text = "${position + 1}."
                } else {
                    if (position - 1 > recipe.stepsForCooking[0].size) {
                        stepContent =
                            recipe.stepsForCooking[1][position - recipe.stepsForCooking[0].size - 2]
                        (holder as StepViewHolder).numOfStep.text = "${position - recipe.stepsForCooking[0].size - 1}."
                    } else {
                        stepContent = recipe.stepsForCooking[0][position - 1]
                        (holder as StepViewHolder).numOfStep.text = "$position."
                    }
                }
                (holder as StepViewHolder).stepContent.text = stepContent
            }
            else -> {
                if (position == 0)
                    (holder as SubrecipeViewHolder).name.text = recipe.subRecipes[0]
                else
                    (holder as SubrecipeViewHolder).name.text = recipe.subRecipes[1]
            }
        }

    }

    override fun getItemCount(): Int =
        if (recipe.subRecipes.size == 0)
            recipe.stepsForCooking[0].size
        else
            recipe.stepsForCooking[0].size + recipe.stepsForCooking[1].size + 2
}