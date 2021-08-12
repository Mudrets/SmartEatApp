package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.vh.IngredientViewHolder

class WhatNeedAdapter(
    private val whatNeed: ArrayList<String>
): RecyclerView.Adapter<IngredientViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return R.layout.ingredients_row
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.weightTextView.text = ""
        holder.weightTextView.visibility = View.GONE
        holder.nameTextView.text = "- ${whatNeed[position]}"
    }

    override fun getItemCount(): Int = whatNeed.size

}