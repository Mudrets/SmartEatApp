package com.example.smarteat.models

import com.example.smarteat.utils.RecipesIngredientsInfo
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable
import java.lang.ClassCastException
import kotlin.math.roundToInt

class Eating(
    _name: String,
    _recipeNum: Int = 0,
    _ingredients: ArrayList<Ingredient> = ArrayList()
): Serializable {
    internal var recipeNum: Int = _recipeNum
        private set
    internal val ingredients: ArrayList<Ingredient> = _ingredients
    internal val name: String = _name
    internal val recipes: ArrayList<Recipe> = ArrayList()
    private val cookInfoIds = RecipesIngredientsInfo.recipesIds[recipeName]
    internal var isExpanded = true

    init {
        if (cookInfoIds != null)
            for (cookInfoId in cookInfoIds)
                if (RecipesIngredientsInfo.recipes[cookInfoId] != null)
                    recipes.add(RecipesIngredientsInfo.recipes[cookInfoId]!!)
    }

    internal constructor(data: JSONObject, name: String): this(_name = name, _recipeNum = data.getInt("recipe")){
        val ingredientsFromData = data["list"] as JSONArray
        for (i in 0 until ingredientsFromData.length()) {
            ingredients.add(Ingredient(
                ingredientsFromData.getJSONObject(i))
            )
        }
    }

    internal val calories: Double
        get() {
            var sumCalories = 0.0
            for (ingredient in ingredients)
                sumCalories += ingredient.calories
            return sumCalories
        }

    internal val proteins: Double
        get() {
            var sumProteins = 0.0
            for (ingredient in ingredients)
                sumProteins += ingredient.proteins
            return sumProteins
        }

    internal val fats: Double
        get() {
            var sumFats = 0.0
            for (ingredient in ingredients)
                sumFats += ingredient.fats
            return sumFats
        }

    internal val carbons: Double
        get() {
            var sumCarbons = 0.0
            for (ingredient in ingredients)
                sumCarbons += ingredient.carbons
            return sumCarbons
        }

    internal val stringProperties: String
        get() {
            return "Б${proteins.roundToInt()}  Ж${fats.roundToInt()}  " +
                    "У${carbons.roundToInt()}  ККАЛ${calories.roundToInt()}"
        }

    internal val recipeName: String
        get() {
            val recipeName = RecipesIngredientsInfo.recipesNames[recipeNum] ?: "Рецепт"
            if (recipeName.contains("Каша") &&
                    !(recipeName.contains("на молоке") ||
                    recipeName.contains("на воде")))
                recipeName.replace("Каша", "Каша на воде")
            return recipeName
        }

    internal fun copy(): Eating {
        val copiedIngredients = ArrayList<Ingredient>()
        for (ingredient in ingredients)
            copiedIngredients.add(ingredient.copy())
        return Eating(
            _name = name,
            _recipeNum =  recipeNum,
            _ingredients = copiedIngredients
        )
    }
}
