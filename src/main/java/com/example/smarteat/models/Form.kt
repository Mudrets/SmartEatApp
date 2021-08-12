package com.example.smarteat.models

import org.json.JSONObject
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class Form(
    _age: Int,
    _cookingsPerWeek: Int,
    _fitnessLevel: Int,
    _gender: Int,
    _goal: Int,
    _height: Int,
    _lifeStyle: Int,
    _weight: Int,
    _workoutsPerWeek: Int,
    _createdAt: LocalDateTime = LocalDateTime.now(),
    _updatedAt: LocalDateTime = LocalDateTime.now(),
    _ingredients: ArrayList<String> = ArrayList(),
    _name: String = "Незаконченная анкета",
    _isActive: Boolean = false,
    _hasPlan: Boolean = false,
    _motherhood: Int = if (_gender == 0) 0 else -1
) : Serializable {

    constructor(data: JSONObject) : this(
        _age = if (data.has("age")) data.getInt("age") else -1,
        _cookingsPerWeek = if (data.has("cookingsPerWeek")) data.getInt("cookingsPerWeek") else -1,
        _fitnessLevel = if (data.has("fitnessLevel")) data.getInt("fitnessLevel") else -1,
        _goal = if (data.has("goal")) data.getInt("goal") else -1,
        _height = if (data.has("height")) data.getInt("height") else -1,
        _lifeStyle = if (data.has("lifestyle")) data.getInt("lifestyle") else -1,
        _weight = if (data.has("weight")) data.getInt("weight") else -1,
        _workoutsPerWeek = if (data.has("workoutsPerWeek")) data.getInt("workoutsPerWeek") else -1,
        _gender = if (data.has("gender")) data.getInt("gender") else -1
    ) {
        if (data.has("fullName"))
            name = data.getString("fullName")
        val ingredientsFromData = data.getJSONArray("ingredients")
        if (data.has("motherhood") && gender == 0)
            motherhood = data.getInt("motherhood")

        if (data.has("dailyPFC")) {
            val dailyPFC = data.getJSONObject("dailyPFC")
            dailyProtein = dailyPFC.getDouble("p").toInt()
            dailyFat = dailyPFC.getDouble("f").toInt()
            dailyCarbon = dailyPFC.getDouble("c").toInt()
            dailyCal = dailyProtein * 4 + dailyFat * 9 + dailyCarbon * 4
        }

        for (i in 0 until ingredientsFromData.length())
            ingredients.add(ingredientsFromData.getString(i))
        hasPlan = isComplete
    }

    private val isComplete: Boolean
        get() {
            val motherhoodCorrect = gender == 1 || (gender == 0 && motherhood > -1)
            return  age > -1 &&
                    cookingsPerWeek > -1 &&
                    fitnessLevel > -1 &&
                    goal > -1 &&
                    height > -1 &&
                    lifeStyle > -1 &&
                    weight > -1 &&
                    workoutsPerWeek > -1 &&
                    gender > -1 &&
                    motherhoodCorrect
        }

    internal var isActive: Boolean = _isActive
    internal var hasPlan = _hasPlan
    internal var name: String = _name
    internal var age: Int = _age
    internal var cookingsPerWeek: Int = _cookingsPerWeek
    internal var fitnessLevel: Int = _fitnessLevel
    internal var gender: Int = _gender
    internal var goal: Int = _goal
    internal var height: Int = _height
    internal var ingredients: ArrayList<String> = _ingredients
    internal var lifeStyle: Int = _lifeStyle
    internal var weight: Int = _weight
    internal var workoutsPerWeek: Int = _workoutsPerWeek
    internal var createdAt = _createdAt
    internal var updatedAt = _updatedAt
    internal var email = ""
    internal var motherhood = _motherhood
    internal var dailyProtein: Int = -1
    internal var dailyFat: Int = -1
    internal var dailyCarbon: Int = -1
    internal var dailyCal: Int = -1
    internal var baseCal: Int = -1;
    internal var snacks: ArrayList<Boolean> = ArrayList(Arrays.asList(false, false, false))

    internal fun copy(): Form {
        return Form(
            _age = age,
            _cookingsPerWeek = cookingsPerWeek,
            _fitnessLevel = fitnessLevel,
            _goal = goal,
            _height = height,
            _lifeStyle = lifeStyle,
            _weight = weight,
            _workoutsPerWeek = workoutsPerWeek,
            _gender = gender,
            _createdAt = createdAt,
            _updatedAt = LocalDateTime.now(),
            _ingredients = ingredients,
            _name = name,
            _isActive = false,
            _hasPlan = hasPlan
        )
    }

    companion object {
        fun createEmptyForm(): Form {
            return Form(
                _age = -1,
                _cookingsPerWeek = -1,
                _fitnessLevel = -1,
                _goal = -1,
                _height = -1,
                _lifeStyle = -1,
                _weight = -1,
                _workoutsPerWeek = -1,
                _gender = -1
            )
        }
    }
}