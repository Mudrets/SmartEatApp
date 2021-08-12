package com.example.smarteat.utils

import com.example.smarteat.models.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.lang.reflect.Array
import java.time.format.DateTimeFormatter

object JsonConverter {

    fun formToJsonWithoutDailyPFC(form: Form): JSONObject {
        val jsonForm = JSONObject()
        jsonForm.put("fullName", form.name)
        jsonForm.put("gender", form.gender)
        if (form.age != -1)
            jsonForm.put("age", form.age)
        if (form.height != -1)
            jsonForm.put("height", form.height)
        if (form.weight != -1)
            jsonForm.put("weight", form.weight)
        if (form.goal != -1)
            jsonForm.put("goal", form.goal)
        if (form.workoutsPerWeek != -1)
            jsonForm.put("workoutsPerWeek", form.workoutsPerWeek)
        if (form.lifeStyle != -1)
            jsonForm.put("lifestyle", form.lifeStyle)
        if (form.fitnessLevel != -1)
            jsonForm.put("fitnessLevel", form.fitnessLevel)
        if (form.cookingsPerWeek != -1)
            jsonForm.put("cookingsPerWeek", form.cookingsPerWeek)
        if (form.gender != -1)
            jsonForm.put("gender", form.gender)
        if (form.gender == 0 && form.motherhood != -1)
            jsonForm.put("motherhood", form.motherhood)

        val jsonArrayIngredients = JSONArray()
        for (ingredient in form.ingredients)
            jsonArrayIngredients.put(ingredient)

        jsonForm.put("ingredients", jsonArrayIngredients)

        val snacksArr = JSONArray()
        if (form.snacks.contains(true)) {
            if (form.snacks[0])
                snacksArr.put("snack1")
            if (form.snacks[1])
                snacksArr.put("snack1")
            if (form.snacks[2])
                snacksArr.put("snack1")
        } else
            snacksArr.put("without")
        jsonForm.put("snacks", snacksArr)

        return jsonForm
    }

    fun formToJson(form: Form): JSONObject {
        val jsonForm = formToJsonWithoutDailyPFC(form)

        val dailyPFC = JSONObject()
        dailyPFC.put("p", form.dailyProtein)
        dailyPFC.put("f", form.dailyFat)
        dailyPFC.put("c", form.dailyCarbon)
        jsonForm.put("dailyPFC", dailyPFC)

        return jsonForm
    }

    fun ingredientToJson(ingredient: Ingredient): JSONObject {
        val jsonIngredient = JSONObject()
        jsonIngredient.put("name", ingredient.name)
        jsonIngredient.put("weight", ingredient.weight)
        jsonIngredient.put("isBought", ingredient.isBought)
        return jsonIngredient
    }

    fun eatingToJson(eating: Eating): JSONObject {
        val jsonEating = JSONObject()
        jsonEating.put("recipe", eating.recipeNum)
        val ingredientsJsonArray = JSONArray()
        for (ingredient in eating.ingredients)
            ingredientsJsonArray.put(ingredientToJson(ingredient))
        jsonEating.put("list", ingredientsJsonArray)
        return jsonEating
    }

    fun dayToJson(day: Day): JSONObject {
        val dayJson = JSONObject()
        val indexes = arrayOf(0, 2, 1, 3, 5, 4)
        val names = ArrayList<String>(listOf(
            "breakfast",
            "snack1",
            "dinner",
            "snack2",
            "supper",
            "snack3"
        ))
        for (i in 0..5)
            dayJson.put(names[day.eating[i].recipeNum / 100 - 1], eatingToJson(day.eating[i]))
        return dayJson
    }

    fun planToJson(plan: Plan): JSONObject {
        val jsonPlan = JSONObject()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val createdAtStr = formatter.format(plan.createdAt)
        jsonPlan.put("createdAt", createdAtStr)
        val updatedAtStr = formatter.format(plan.updateAt)
        jsonPlan.put("updatedAt", updatedAtStr)
        jsonPlan.put("profile", formToJson(plan.form))
        if (plan.activatedAt != null)
            jsonPlan.put("activatedAt", plan.activatedAt.toString())
        val jsonDayArray = JSONArray()
        for (day in plan.days)
            jsonDayArray.put(dayToJson(day))
        jsonPlan.put("days", jsonDayArray)
        return jsonPlan
    }

    fun productsHolderToJson(productsHolder: ProductsHolder): JSONObject {
        val phJson = JSONObject()
        phJson.put("categoryName", productsHolder.categoryName)
        val productsArr = JSONArray()
        for (product in productsHolder.boughtProducts)
            productsArr.put(ingredientToJson(product))
        for (product in productsHolder.notBoughtProducts)
            productsArr.put(ingredientToJson(product))
        phJson.put("products", productsArr)
        return phJson
    }

    fun jsonToProductsHolder(json: JSONObject): ProductsHolder {
        return if (json.has("categoryName") && json.has("products")) {
            val categoryName = json.getString("categoryName")
            val products = ArrayList<Ingredient>()
            for (i in 0 until json.getJSONArray("products").length())
                products.add(Ingredient(json.getJSONArray("products").getJSONObject(i)))
            ProductsHolder(categoryName, products)
        } else
            ProductsHolder("", ArrayList())
    }

}