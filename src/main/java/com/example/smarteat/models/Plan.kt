package com.example.smarteat.models

import com.example.smarteat.utils.ProductsHolder
import org.json.JSONObject
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class Plan(
    _form: Form = Form.createEmptyForm(),
    _days: ArrayList<Day> = ArrayList(),
    _createdAt: LocalDateTime = LocalDateTime.now(),
    _updatedAt: LocalDateTime = LocalDateTime.now()
) : Serializable {
    internal var form: Form = _form
    internal var createdAt: LocalDateTime = _createdAt
        private set
    internal var updateAt: LocalDateTime = _updatedAt
        private set
    internal var activatedAt: LocalDate? = null
    internal var days: ArrayList<Day> = _days
        private set
    internal val weeks: ArrayList<ArrayList<Day>>
        get() {
            val res = ArrayList<ArrayList<Day>>()
            if (days.size == 28) {
                for (i in 0..3)
                    res.add(ArrayList())

                var counter = 0
                for (day in days)
                    res[counter++ / 7].add(day)
            }
            return res
        }
    internal val productsForWeek: ArrayList<Ingredient>
        get() {
            val products = ArrayList<Ingredient>()
            if (activatedAt != null) {
                for (day in weeks[numOfWeek - 1]) {
                    for (eatings in day.eating) {
                        for (ingredient in eatings.ingredients) {
                            if (products.contains(ingredient)) {
                                val index = products.indexOf(ingredient)
                                products[index].weight += ingredient.weight
                            } else
                                products.add(ingredient.copy())
                        }
                    }
                }
            }
            return products
        }

    internal val currDay: Int
        get() {
            if (activatedAt != null) {
                val now = LocalDate.now()
                var activeAt = activatedAt!!
                var day = 1
                while (activeAt < now && day < 29) {
                    activeAt = activeAt.plusDays(1)
                    day++
                }
                if (day == 29)
                    return -1
                return day
            }
            return -1
        }

    internal val recipes: ArrayList<ArrayList<Recipe>>
        get() {
            val recipes = ArrayList<ArrayList<Recipe>>()
            for (i in 0..5)
                recipes.add(ArrayList())
            for (day in weeks[numOfWeek - 1])
                for ((i, recipeCategory) in day.recipes.withIndex())
                    for (recipe in recipeCategory)
                        if (!recipes[i].contains(recipe))
                            recipes[i].add(recipe.copy())
            return recipes
        }

    internal val numOfWeek: Int
        get() {
            if (activatedAt != null) {
                val now = LocalDate.now()
                var activeAt = activatedAt!!
                var numOfWeek = 0
                while (activeAt <= now) {
                    activeAt = activeAt.plusWeeks(1)
                    numOfWeek++
                }
                return (numOfWeek - 1) % 4 + 1
            }
            return -1
        }

    internal constructor(data: JSONObject) : this() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val createAtString = data["createdAt"] as? String ?: ""
        if (createAtString != "")
            createdAt = LocalDateTime.parse(createAtString, formatter)
        else
            createdAt = LocalDateTime.now()

        val updateArString = if (data.has("updatedAt"))
            data.getString("updatedAt")
        else
            createAtString
        updateAt = LocalDateTime.parse(updateArString, formatter)

        if (data.has("activatedAt")) {
            val activateAtString = data.getString("activatedAt")
            activatedAt = LocalDate.parse(activateAtString)
            if (activatedAt!!.plusWeeks(4) < LocalDate.now())
                activatedAt = null;
        } else
            activatedAt = null

        val daysFromData = data.getJSONArray("days")
        for (i in 0 until daysFromData.length())
            days.add(Day(daysFromData.getJSONObject(i)))

        form = Form(data.getJSONObject("profile"))
        form.createdAt = createdAt
        form.updatedAt = updateAt
    }

    internal fun getRecipesForDay(num: Int): ArrayList<ArrayList<Recipe>> {
        if (activatedAt == null)
            return ArrayList()
        return if (num == 0 || num > 28)
            days[currDay].recipes
        else
            days[num].recipes
    }

    internal fun copy(): Plan {
        val copiedDays = ArrayList<Day>()
        for (day in days) {
            copiedDays.add(day.copy())
        }

        return Plan(
            _form = form.copy(),
            _days = copiedDays,
            _createdAt = createdAt,
            _updatedAt = updateAt
        )
    }
}