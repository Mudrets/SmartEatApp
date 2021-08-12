package com.example.smarteat.utils

import com.example.smarteat.models.Ingredient

class ProductsHolder(
    val categoryName: String,
    private val products: ArrayList<Ingredient>
) {
    val boughtProducts = ArrayList<Ingredient>()
    val notBoughtProducts = ArrayList<Ingredient>()
    val size = products.size

    init {
        for (product in products) {
            if (product.isBought)
                boughtProducts.add(product)
            else
                notBoughtProducts.add(product)
        }
    }

    fun update() {
        boughtProducts.clear()
        notBoughtProducts.clear()
        for (product in products) {
            if (product.isBought)
                boughtProducts.add(product)
            else
                notBoughtProducts.add(product)
        }
    }
}