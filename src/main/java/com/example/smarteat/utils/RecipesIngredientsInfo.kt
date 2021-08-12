package com.example.smarteat.utils

import com.example.smarteat.models.IngredientInfo
import com.example.smarteat.models.Recipe
import org.json.JSONArray
import org.json.JSONException

object RecipesIngredientsInfo {
    internal val ingredientsInfo = HashMap<String, IngredientInfo>()
    internal val recipesNames = HashMap<Int, String>()
    internal val recipes = HashMap<String, Recipe>()
    internal val recipesIds = HashMap<String, ArrayList<String>>()
    internal val quizNameToName = HashMap<String, String>()

    fun getIngredientsInfoFromJSON(data: JSONArray): Boolean {
        return try {
            for (i in 0 until data.length()) {
                val jsonObject = data.getJSONObject(i)
                val ingredientInfo = IngredientInfo(
                    calories = jsonObject.getDouble("calories"),
                    proteins = jsonObject.getDouble("proteins"),
                    fats = jsonObject.getDouble("fats"),
                    carbons = jsonObject.getDouble("carbons"),
                    shopQuant = jsonObject.getString("shopQuant"),
                    name = jsonObject.getString("name"),
                    shopName = jsonObject.getString("shopName"),
                    quizName = jsonObject.getString("nameForQuiz")
                )
                ingredientsInfo[jsonObject.getString("name")] = ingredientInfo
                quizNameToName[ingredientInfo.quizName] = ingredientInfo.name
            }
            true
        } catch (ex: JSONException) {
            false
        }
    }

    fun getRecipesNamesAndCookInfoIds(data: JSONArray): Boolean {
        return try {
            for (i in 0 until data.length()) {
                val jsonObject = data.getJSONObject(i)
                val cookInfoIds = ArrayList<String>()
                val cookInfoList = jsonObject.getJSONArray("list")
                for (j in 0 until cookInfoList.length())
                    if (cookInfoList.getJSONObject(j).has("cookInfo"))
                        cookInfoIds.add(cookInfoList.getJSONObject(j).getString("cookInfo"))
                val recipeIndex = jsonObject.getInt("recipeIndex")
                val recipeName = jsonObject.getString("name")
                recipesNames[recipeIndex] = recipeName
                recipesIds[recipeName] = cookInfoIds
            }
            true
        } catch (ex: JSONException) {
            false
        }
    }
}