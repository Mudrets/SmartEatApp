package com.example.smarteat.utils

import android.os.Environment
import com.example.smarteat.models.Form
import com.example.smarteat.models.User
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.IllegalArgumentException
import java.time.format.DateTimeFormatter
import java.util.*

object DataUpdater {

    private fun createEmptyPlan(uuid: String, createdAt: String, form: JSONObject): JSONObject {
        val jsonPlan = JSONObject()
        jsonPlan.put("createdAt", createdAt)
        jsonPlan.put("userId", uuid)
        val planId = UUID.randomUUID().toString()
        jsonPlan.put("planId", planId)
        jsonPlan.put("days", JSONArray())
        jsonPlan.put("profile", form)
        return jsonPlan
    }

    suspend fun addFormWithoutPlanToData(userDataFile: File, form: Form) {
        if (form.hasPlan)
            throw IllegalArgumentException("passed form has plan")

        val jsonForm = JsonConverter.formToJson(form)

        val stringUserData = ReadWriter.readDataFromFile(userDataFile)
        val userJson = JSONObject(stringUserData)
        val uuid = userJson
            .getJSONArray("users")
            .getJSONObject(0)
            .getString("userId")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val stringDate = formatter.format(form.createdAt)

        val jsonPlan = createEmptyPlan(uuid, stringDate, jsonForm)
        userJson.getJSONArray("plans").put(jsonPlan)

        ReadWriter.writeDataToFile(userDataFile, userJson.toString())
    }

    private suspend fun createPlanForForm(uuid: String, form: Form): JSONObject {
        val jsonForm = JsonConverter.formToJson(form)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val stringDate = formatter.format(form.createdAt)

        return createEmptyPlan(uuid, stringDate, jsonForm)
    }

    suspend fun updateAllPlans(userDataFile: File, user: User) {
        val stringUserData = ReadWriter.readDataFromFile(userDataFile)
        val userJson = JSONObject(stringUserData)

        val jsonPlansArr = JSONArray()
        for (plan in user.plans)
            jsonPlansArr.put(JsonConverter.planToJson(plan))
        for (form in user.formsWithoutPlan) {
            jsonPlansArr.put(createPlanForForm(userJson
                .getJSONArray("users")
                .getJSONObject(0)
                .getString("userId"), form))
        }
        if (user.activePlan != null)
            jsonPlansArr.put(JsonConverter.planToJson(user.activePlan!!))
        userJson.put("plans", jsonPlansArr)

        ReadWriter.writeDataToFile(userDataFile, userJson.toString())
    }

    suspend fun updateAchievements(userDataFile: File, user: User) {
        val userJson = JSONObject(ReadWriter.readDataFromFile(userDataFile))
        val jsonAchievementFlagsArr = JSONArray()
        for (f in user.achievementFlags)
            jsonAchievementFlagsArr.put(f)
        userJson.put("achievements", jsonAchievementFlagsArr)
        ReadWriter.writeDataToFile(userDataFile, userJson.toString())
    }

    suspend fun updateOtherInfo(userDataFile: File, user: User) {
        val userJson = JSONObject(ReadWriter.readDataFromFile(userDataFile))
        userJson.getJSONArray("users").getJSONObject(0).put("firstUserEnterDay", user.firstUserEnterDay.toString())
        userJson.getJSONArray("users").getJSONObject(0).put("fullName", user.fullName)
        if (user.productsForWeek != null) {
            val phsJson = JSONArray()
            for (ph in user.productsForWeek!!)
                phsJson.put(JsonConverter.productsHolderToJson(ph))
            userJson.getJSONArray("users").getJSONObject(0)
                .put("productsForWeek", phsJson)
        }
        if (user.updatedProductsForWeekAt != null) {
            userJson.getJSONArray("users").getJSONObject(0)
                .put("updatedProductsForWeekAt", user.updatedProductsForWeekAt.toString())
        }
        if (user.subscribeDate != null) {
            userJson.getJSONArray("users").getJSONObject(0)
                .put("subscribeDate", user.subscribeDate)
        }
        ReadWriter.writeDataToFile(userDataFile, userJson.toString())
    }

    fun saveExtraInfoAboutUser(user: User, dir: File) {
        val extraInfoJson = JSONObject()
        val jsonAchievementFlagsArr = JSONArray()
        for (f in user.achievementFlags)
            jsonAchievementFlagsArr.put(f)
        extraInfoJson.put("achievements", jsonAchievementFlagsArr)
        extraInfoJson.put("fullName", user.fullName)
        extraInfoJson.put("phone", user.phone)
        if (user.activePlan != null) {
            val activeJson = JsonConverter.planToJson(user.activePlan!!)
            extraInfoJson.put("activePlan", activeJson)
        }
        if (user.productsForWeek != null) {
            val phsJson = JSONArray()
            for (ph in user.productsForWeek!!)
                phsJson.put(JsonConverter.productsHolderToJson(ph))
            extraInfoJson.put("productsForWeek", phsJson)
        }
        if (user.subscribeDate != null) {
            extraInfoJson.put("subscribeDate", user.subscribeDate)
        }
        extraInfoJson.put("firstUserEnterDay", user.firstUserEnterDay)
        extraInfoJson.put("showFormWarnings", user.showFormsWarnings)
        val extraFile = File(dir, "${user.userId}.json")
        ReadWriter.writeDataToFile(extraFile, extraInfoJson.toString())
    }
}