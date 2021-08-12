package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.utils.ProductsHolder
import com.example.smarteat.vh.HeaderViewHolder
import com.example.smarteat.vh.MarginViewHolder
import com.example.smarteat.vh.ProductCategoriesViewHolder

class ProductCategoriesAdapter(
    private val productHolders: ArrayList<ProductsHolder> = ArrayList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return R.layout.header_row
        if (position == productHolders.size + 1)
            return R.layout.margin_row
        return R.layout.category_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        if (viewType == R.layout.category_row)
            return ProductCategoriesViewHolder(view)
        if (viewType == R.layout.margin_row)
            return MarginViewHolder(view)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductCategoriesViewHolder) {
            val productHolder = productHolders[position - 1]
            holder.categoryName.text = productHolder.categoryName
            holder.categoryProducts.adapter = ProductAdapter(productHolder)
            if (productHolder.size == 0) {
                holder.categoryHeader.visibility = View.GONE
                holder.categoryProducts.visibility = View.GONE
            }
        } else if (holder is HeaderViewHolder)
            holder.header.text = "Список продуктов"
    }

    override fun getItemCount(): Int = productHolders.size + 2

}