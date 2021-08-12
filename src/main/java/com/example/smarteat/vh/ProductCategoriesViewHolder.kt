package com.example.smarteat.vh

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R

class ProductCategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val categoryHeader: ViewGroup = itemView.findViewById(R.id.category_row__header)
    val categoryName: TextView = itemView.findViewById(R.id.category_row__header_text)
    val categoryProducts: RecyclerView = itemView.findViewById(R.id.category_row__products_list)
    init {
        categoryProducts.layoutManager = LinearLayoutManager(itemView.context)
    }
}