package com.example.smarteat.models

import org.json.JSONObject
import java.io.Serializable
import kotlin.collections.ArrayList

class Plan(data: JSONObject) : Serializable {
    private val form: Form
    var days: ArrayList<Day> = ArrayList()
        private set
    var weeks: ArrayList<ArrayList<Day>> = ArrayList()
        private set
    init {
        val daysFromData = data.getJSONArray("days")
        for (i in 0 until daysFromData.length())
            days.add(Day(daysFromData.getJSONObject(i)))

        form = Form(data.getJSONObject("profile"))
        for (i in 0..3)
            weeks.add(ArrayList())

        var counter = 0
        for (day in days)
            weeks[counter++ / 7].add(day)
    }

    fun updateForm(newData: Map<String, Any>) {
        form.age = newData["age"] as Int? ?: form.age
        form.cookingsPerWeek = newData["cookingPerWeek"] as Int? ?: form.cookingsPerWeek
        form.fitnessLevel = newData["fitnessLevel"] as Int? ?: form.fitnessLevel
        form.gender = newData["gender"] as Int? ?: form.gender
        form.goal = newData["goal"] as Int? ?: form.goal
        form.height = newData["height"] as Int? ?: form.height
        form.ingredients = newData["ingredients"] as ArrayList<String>? ?: form.ingredients
        form.lifeStyle = newData["lifeStyle"] as Int? ?: form.lifeStyle
        form.motherhood = newData["motherhood"] as Int? ?: form.motherhood
        form.weight = newData["weight"] as Int? ?: form.weight
        form.workoutsPerWeek = newData["workoutsPerWeek"] as Int? ?: form.workoutsPerWeek
    }
}