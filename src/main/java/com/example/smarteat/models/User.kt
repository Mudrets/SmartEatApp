package com.example.smarteat.models

import com.example.smarteat.utils.AchievementChecker
import com.example.smarteat.utils.DataUpdater
import com.example.smarteat.utils.JsonConverter
import com.example.smarteat.utils.ProductsHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class User(data: JSONObject) : Serializable, AchievementChecker.AchievementListener {
    private val createdAt: LocalDateTime
    internal var showFormsWarnings = true
    internal val userId: String
    internal val email: String
    internal val plans: ArrayList<Plan> = ArrayList()
    internal val formsWithoutPlan: ArrayList<Form> = ArrayList()
    internal val firstUserEnterDay: LocalDate
    internal var isVisit: Boolean = false
    internal var fullName: String
    internal var phone: String
    internal val boughtAllProducts: Boolean
        get() {
            if (activePlan == null || productsForWeek == null)
                return false

            for (ph in productsForWeek!!)
                if (ph.notBoughtProducts.size > 0)
                    return false
            return true
        }
    internal var isSubscribed: Boolean = false
        set(value) {
            if (value) {
                subscribeDate = LocalDate.now()
                subscribeEndDate = LocalDate.now().plusMonths(1)
            } else {
                subscribeDate = null
                subscribeEndDate = null
            }
            field = value
        }
    internal var subscribeDate: LocalDate? = null
    internal var subscribeEndDate: LocalDate? = null
    internal var updatedProductsForWeekAt: LocalDate? = null
    internal var numOfWeekProducts: Int = 1
    internal var productsForWeek: ArrayList<ProductsHolder>? = null
        set(value) {
            field = value
            updatedProductsForWeekAt = if (value != null)
                LocalDate.now()
            else
                null
        }
    internal var activePlan: Plan? = null
        internal set(value) {
            if (field != null) {
                plans.add(0, field!!)
                field!!.form.isActive = false
                field!!.activatedAt = null
            }
            if (value != null) {
                plans.remove(value)
                value.form.isActive = true
                if (value.activatedAt == null)
                    value.activatedAt = LocalDate.now()
            }
            field = value
            productsForWeek = null
            AchievementChecker.checkAchievements()
        }
    internal val achievements = ArrayList<Achievement>()
    internal val achievementFlags = ArrayList<Boolean>()
    internal val isFirstWeek: Boolean
        get() {
            return firstUserEnterDay.plusWeeks(1) > LocalDate.now()
        }


    init {
        email = if (data.getJSONObject("login").has("value")) {
            data
                .getJSONObject("login")
                .getJSONObject("value")
                .getString("email")
        } else {
            data
                .getJSONObject("login")
                .getJSONObject("params")
                .getString("email")
        }

        phone = if (data.getJSONArray("users").getJSONObject(0).has("phone"))
            data.getJSONArray("users").getJSONObject(0).getString("phone")
        else
            ""

        fullName = if (data.getJSONArray("users").getJSONObject(0).has("fullName"))
            data.getJSONArray("users").getJSONObject(0).getString("fullName")
        else
            email.split("@")[0]

        userId = data.getJSONArray("users")
            .getJSONObject(0)
            .getString("userId")

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        createdAt = LocalDateTime.parse(
            data.getJSONArray("users")
                .getJSONObject(0)
                .getString("createdAt"),
            formatter
        )

        if (data.getJSONArray("users")
                .getJSONObject(0)
                .has("firstUserEnterDay")
        ) {
            firstUserEnterDay = LocalDate.parse(
                data.getJSONArray("users")
                    .getJSONObject(0)
                    .getString("firstUserEnterDay")
            )
            isVisit = true
        } else
            firstUserEnterDay = LocalDate.now()

        val plansFromData = data.getJSONArray("plans")
        for (i in 0 until plansFromData.length())
            plans.add(Plan(plansFromData.getJSONObject(i)))

        var index = 0
        val size = plans.size
        for (i in 0 until size) {
            if (!plans[index].form.hasPlan || plans[index].days.count() < 28) {
                plans[index].form.hasPlan = false
                formsWithoutPlan.add(plans[index].form)
                plans.removeAt(index--)
            } else if (plans[index].form.hasPlan && plans[index].days.count() == 28 && plans[index].activatedAt != null)
                activePlan = plans[index--]
            index++
        }

        for (i in 0 until AchievementChecker.achievements.size)
            achievementFlags.add(false)

        if (data.has("achievements"))
            for (i in 0 until achievementFlags.size)
                achievementFlags[i] = data.getJSONArray("achievements").getBoolean(i)

        for ((i, f) in achievementFlags.withIndex())
            if (f)
                achievements.add(AchievementChecker.achievements[i])

        if (data.has("activePlan")) {
            activePlan = Plan(data.getJSONObject("activePlan"))
            if (activePlan != null && activePlan!!.currDay > 0)
                plans.removeIf { plan ->
                    plan.createdAt == activePlan?.createdAt && plan.updateAt == activePlan?.updateAt
                }
            else
                activePlan = null
        }

        if (data.getJSONArray("users").getJSONObject(0)
                .has("productsForWeek")
        ) {
            val phsJson = data.getJSONArray("users")
                .getJSONObject(0)
                .getJSONArray("productsForWeek")
            val phs = ArrayList<ProductsHolder>()
            for (i in 0 until phsJson.length())
                phs.add(JsonConverter.jsonToProductsHolder(phsJson.getJSONObject(i)))
            if (phs.size > 0)
                productsForWeek = phs
            updatedProductsForWeekAt = LocalDate.now()
        }

        if (data.getJSONArray("users").getJSONObject(0)
                .has("updatedProductsForWeekAt")
        ) {
            val strDate = data.getJSONArray("users").getJSONObject(0)
                .getString("updatedProductsForWeekAt")
            updatedProductsForWeekAt = LocalDate.parse(strDate)
        }

        if (data.getJSONArray("users").getJSONObject(0).has("subscribeDate")) {
            subscribeDate = LocalDate.parse(
                data.getJSONArray("users")
                    .getJSONObject(0)
                    .getString("subscribeDate")
            )
            subscribeEndDate = subscribeDate?.plusMonths(1)
            if (subscribeEndDate != null && subscribeEndDate!! > LocalDate.now())
                isSubscribed = true
        }

        if (data.getJSONArray("users").getJSONObject(0).has("showFormWarnings"))
            showFormsWarnings = data.getJSONArray("users").getJSONObject(0).getBoolean("showFormWarnings")

    }

    override fun userGetAchievement(achievement: Achievement) {
        achievements.add(achievement)
        val index = AchievementChecker.achievements.indexOf(achievement)
        achievementFlags[index] = true
    }
}