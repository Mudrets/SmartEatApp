package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Ingredient
import com.example.smarteat.utils.AchievementChecker
import com.example.smarteat.utils.ProductsHolder
import com.example.smarteat.vh.BoughtProductViewHolder
import com.example.smarteat.vh.NotBoughtProductViewHolder
import com.example.smarteat.vh.ProductViewHolder

class ProductAdapter(
    private val productsHolder: ProductsHolder
): RecyclerView.Adapter<ProductViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return if (position < productsHolder.notBoughtProducts.size)
            R.layout.not_bought_product_row
        else
            R.layout.bought_product_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.not_bought_product_row -> NotBoughtProductViewHolder(view)
            else -> BoughtProductViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val ingredient: Ingredient = if (position < productsHolder.notBoughtProducts.size)
            productsHolder.notBoughtProducts[position]
        else
            productsHolder.boughtProducts[position - productsHolder.notBoughtProducts.size]
        holder.productName.text = ingredient.shopName
        holder.weight.text = ingredient.weightAsStringWithUnit
        holder.radioButton.isChecked = ingredient.isBought
        holder.productCard.setOnClickListener {
            ingredient.isBought = !ingredient.isBought
            productsHolder.update()
            AchievementChecker.checkAchievements()
            notifyDataSetChanged()
        }
        holder.radioButton.setOnClickListener {
            ingredient.isBought = !ingredient.isBought
            productsHolder.update()
            AchievementChecker.checkAchievements()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = productsHolder.size
}