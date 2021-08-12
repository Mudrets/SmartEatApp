package com.example.smarteat.models

data class IngredientInfo(
    internal val calories: Double,
    internal val proteins: Double,
    internal val fats: Double,
    internal val carbons: Double,
    internal val shopQuant: String,
    internal val name: String,
    internal val shopName: String,
    internal val quizName: String
){}