package com.example.smarteat.models

import com.example.smarteat.utils.RecipesIngredientsInfo
import org.json.JSONObject
import java.io.Serializable

class Ingredient(
    _name: String = "",
    _weight: Int = 0
) : Serializable {
    internal val name: String = _name
    internal var isBought = false
    internal val nameOfQuiz: String
        get() = RecipesIngredientsInfo.ingredientsInfo[name]?.quizName ?: ""
    internal val shopName: String
        get() = RecipesIngredientsInfo.ingredientsInfo[name]?.shopName ?: ""
    internal var weight: Int = _weight

    internal constructor(data: JSONObject) : this(
        data["name"] as String? ?: "",
        data["weight"] as Int? ?: 0
    ) {
        if (data.has("isBought"))
            isBought = data.getBoolean("isBought")
    }

    internal val calories: Double
        get() {
            val calories100 = RecipesIngredientsInfo.ingredientsInfo[name]?.calories ?: 0.0
            return calories100 * weight / 100.0
        }

    internal val proteins: Double
        get() {
            val proteins100 = RecipesIngredientsInfo.ingredientsInfo[name]?.proteins ?: 0.0
            return proteins100 * weight / 100.0
        }

    internal val fats: Double
        get() {
            val fats100 = RecipesIngredientsInfo.ingredientsInfo[name]?.fats ?: 0.0
            return fats100 * weight / 100.0
        }

    internal val carbons: Double
        get() {
            val carbons100 = RecipesIngredientsInfo.ingredientsInfo[name]?.carbons ?: 0.0
            return carbons100 * weight / 100.0
        }

    internal val weightAsStringWithUnit: String
        get() {
            var units = RecipesIngredientsInfo.ingredientsInfo[name]?.shopQuant ?: "гр. / кг"
            if (units != "гр. / кг" || units != "мл. / л")
                units = "гр. / кг"
            return if (units.contains('/')) {
                val firstUnit = units.split("/")[0].replace(".", " ").trim()
                val secondUnit = units.split("/")[1].trim()
                if (weight < 1000)
                    "$weight $firstUnit"
                else
                    "${Math.round((weight + 99) / 100.0) / 10.0} $secondUnit"
            } else
                "$weight $units"
        }

    internal fun copy(): Ingredient {
        return Ingredient(
            name,
            weight
        )
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Ingredient) {
            shopName == other.shopName
        } else
            super.equals(other)
    }
}
