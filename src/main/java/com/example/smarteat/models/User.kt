package com.example.smarteat.models

import org.json.JSONObject
import java.io.Serializable
import java.time.LocalDate

class User(data: JSONObject): Serializable {
    val email: String = data
        .getJSONObject("login")
        .getJSONObject("value")
        .getString("email")
    val plans: ArrayList<Plan> = ArrayList()
    var activePlan: Plan? = null
    internal set

    init {
        val plansFromData = data.getJSONArray("plans")
        for (i in 0 until plansFromData.length())
            plans.add(Plan(plansFromData.getJSONObject(i)))

        activePlan = plans[1]
    }

    fun addNewPlan(plan: Plan) {
        plans.add(plan)
    }

    fun setActivePlan(index: Int) {
        if (index < plans.size)
            activePlan = plans[index]
        else
            throw IllegalArgumentException()
    }

    fun getPlan(index: Int) : Plan {
        if (index < plans.size)
            return plans[index]
        throw IllegalArgumentException()
    }


}