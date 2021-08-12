package com.example.smarteat.models

import org.json.JSONObject
import java.io.Serializable
import kotlin.math.roundToInt

class Day(
    _eating: ArrayList<Eating> = ArrayList()
): Serializable {
    internal val eating: ArrayList<Eating> = _eating
    internal var isExpanded: Boolean = false
    internal val recipes: ArrayList<ArrayList<Recipe>>
        get() {
            val res = ArrayList<ArrayList<Recipe>>()
            for (e in eating)
                res.add(e.recipes)
            return res
        }
    internal val calories: Int
        get() {
            var caloriesSum = 0.0
            for (currEating in eating)
                caloriesSum += currEating.calories
            return caloriesSum.roundToInt()
        }

    internal constructor(data: JSONObject): this() {
        val gettingArr = ArrayList<Eating>()
        for (key in data.keys())
            gettingArr.add(Eating(data.getJSONObject(key), key))
        val indexes : Array<Int> = arrayOf(
            gettingArr[0].recipeNum / 100 - 1,
            gettingArr[1].recipeNum / 100 - 1,
            gettingArr[2].recipeNum / 100 - 1,
            gettingArr[3].recipeNum / 100 - 1,
            gettingArr[4].recipeNum / 100 - 1,
            gettingArr[5].recipeNum / 100 - 1
        )
        for (index in 0..5)
            eating.add(gettingArr[indexes[index]])
    }

    internal fun copy(): Day {
        val copiedEatings = ArrayList<Eating>()
        for (eating in eating)
            copiedEatings.add(eating.copy())
        return Day(
            _eating = copiedEatings
        )
    }
}
