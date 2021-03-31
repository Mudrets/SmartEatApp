package com.example.smarteat.models

import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable
import java.lang.ClassCastException

class Eating(data: JSONObject, val name: String): Serializable {
    val recipeNum: Int = data["recipe"] as Int? ?: 0
    val ingredients: ArrayList<Ingredient> = ArrayList()
    var isExpanded = false
    internal set
    init {
        val ingredientsFromData = data["list"] as JSONArray
        for (i in 0 until ingredientsFromData.length()) {
            ingredients.add(Ingredient(
                ingredientsFromData.getJSONObject(i))
            )
        }
    }
}
