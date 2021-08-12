package com.example.smarteat.models

data class Recipe(
    internal val name: String,
    internal val id: String,
    internal val subRecipes: ArrayList<String> = ArrayList(),
    internal val stepsForCooking: ArrayList<ArrayList<String>> = ArrayList(),
    internal val whatNeed: ArrayList<String> = ArrayList(),
    internal val advice: String = ""
) {
    internal var isExpanded = false
}