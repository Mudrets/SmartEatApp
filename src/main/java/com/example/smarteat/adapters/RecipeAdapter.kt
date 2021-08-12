package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Recipe
import com.example.smarteat.vh.HeaderViewHolder
import com.example.smarteat.vh.MarginViewHolder
import com.example.smarteat.vh.RecipeDelimiterViewHolder

class RecipeAdapter(
    private val recipesCategories: ArrayList<ArrayList<Recipe>>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val categoriesName = arrayOf(
        "Завтрак",
        "Перекус 1",
        "Обед",
        "Перекус 2",
        "Ужин",
        "Перекус 3"
    )

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView = itemView.findViewById(R.id.recipe_row__recipe_name)
        val countCalories: TextView = itemView.findViewById(R.id.recipe_row__num_calories)
        val arrow: ImageView = itemView.findViewById(R.id.recipe_row__main_arrow)
        val headerWrapper: ViewGroup = itemView.findViewById(R.id.recipe_row__header_wrapper)
        val stepsRecyclerView: RecyclerView = itemView.findViewById(R.id.recipe_row__steps_recycler)
        val whatNeedRecyclerView: RecyclerView = itemView.findViewById(R.id.recipe_row__ingredients_recycler)
        val advice: TextView = itemView.findViewById(R.id.recipe_row__advice_content)
        val howCookHeader: TextView = itemView.findViewById(R.id.recipe_row__how_cook)
        val whatNeedHeader: TextView = itemView.findViewById(R.id.recipe_row__what_need)
        val adviceHeader: TextView = itemView.findViewById(R.id.recipe_row__advice)
        var adviceVisible = true;
        init {
            whatNeedRecyclerView.layoutManager = object : LinearLayoutManager(itemView.context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            stepsRecyclerView.layoutManager = object : LinearLayoutManager(itemView.context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            val clickListener = View.OnClickListener { _ ->
                val (categoryIndex, index) = getCategoryIndex(adapterPosition - 1)
                val recipe = recipesCategories[categoryIndex][index - 1]
                recipe.isExpanded = !recipe.isExpanded
                notifyItemChanged(adapterPosition)
            }
            arrow.setOnClickListener(clickListener)
            headerWrapper.setOnClickListener(clickListener)
        }

        fun setVisibilityToContentPart(visibility: Int) {
            stepsRecyclerView.visibility = visibility
            whatNeedHeader.visibility = visibility
            whatNeedRecyclerView.visibility = visibility
            howCookHeader.visibility = visibility
            if (adviceVisible) {
                advice.visibility = visibility
                adviceHeader.visibility = visibility
            }
        }
    }

    private fun getCategoryIndex(position: Int): Pair<Int, Int> {
        var index = position
        var i = -1
        while (i < 5 && recipesCategories[++i].size + 1 <= index)
            index -= recipesCategories[i].size + 1
        return Pair(i, index)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return R.layout.header_row
        if (position == itemCount - 1)
            return R.layout.margin_row

        val (categoryIndex, index) = getCategoryIndex(position - 1)

        if (index == 0)
            return R.layout.recipe_delimiter_row
        return R.layout.recipe_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.header_row -> HeaderViewHolder(view)
            R.layout.margin_row -> MarginViewHolder(view)
            R.layout.recipe_row -> RecipeViewHolder(view)
            else -> RecipeDelimiterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecipeViewHolder)
            onBindRecipeViewHolder(holder, position)
        else if (holder is RecipeDelimiterViewHolder)
            onBindDelimiterViewHolder(holder, position)
        else if (holder is HeaderViewHolder)
            onBindHeaderHolder(holder)

    }

    private fun onBindRecipeViewHolder(holder: RecipeViewHolder, position: Int) {
        val (categoryIndex, index) = getCategoryIndex(position - 1)
        val recipe = recipesCategories[categoryIndex][index - 1]
        holder.recipeName.text = recipe.name
        holder.countCalories.visibility = View.GONE
        if (recipe.advice.isNotBlank()) {
            holder.advice.text = recipe.advice
            holder.adviceVisible = true
        } else {
            holder.adviceVisible = false
        }
        holder.stepsRecyclerView.adapter = StepAdapter(recipe)
        holder.whatNeedRecyclerView.adapter = WhatNeedAdapter(recipe.whatNeed)
        val isExpanded = recipe.isExpanded
        holder.arrow.rotationX = if (isExpanded) 180F else 0F
        holder.setVisibilityToContentPart(if (isExpanded) View.VISIBLE else View.GONE)
    }

    private fun onBindDelimiterViewHolder(holder: RecipeDelimiterViewHolder, position: Int) {
        val (categoryIndex, index) = getCategoryIndex(position - 1)
        holder.header.text = categoriesName[categoryIndex]
        if (recipesCategories[categoryIndex].isEmpty())
            holder.setVisibility(View.GONE)
        else
            holder.setVisibility(View.VISIBLE)
    }

    private fun onBindHeaderHolder(holder: HeaderViewHolder) {
        holder.header.text = "Рецепты"
    }

    override fun getItemCount(): Int {
        var size = recipesCategories.size
        for (recipes in recipesCategories)
            size += recipes.size
        return size + 2
    }
}