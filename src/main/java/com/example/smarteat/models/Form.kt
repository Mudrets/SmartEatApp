package com.example.smarteat.models

import org.json.JSONObject
import java.io.Serializable


class Form (data: JSONObject): Serializable {
    var age: Int
    internal set

    var cookingsPerWeek: Int
    internal set

    var fitnessLevel: Int
    internal set

    var gender: Int
    internal set

    var goal: Int
    internal set

    var height: Int
    internal set

    var ingredients: ArrayList<String> = ArrayList()
    internal set

    var lifeStyle: Int
    internal set

    var motherhood: Int
    internal set

    var weight: Int
    internal set

    var workoutsPerWeek: Int
    internal set

    init {
        age = data.getInt("age")
        cookingsPerWeek = data.getInt("cookingsPerWeek")
        fitnessLevel = data.getInt("fitnessLevel")
        goal = data.getInt("goal")
        height = data.getInt("height")
        lifeStyle = data.getInt("lifestyle")
        motherhood = data.getInt("motherhood")
        weight = data.getInt("weight")
        workoutsPerWeek = data.getInt("workoutsPerWeek")
        gender = 0
        val ingredientsFromData = data.getJSONArray("ingredients")
        for (i in 0 until ingredientsFromData.length())
            ingredients.add(ingredientsFromData.getString(i))
    }
}