package com.example.smarteat.models

import org.json.JSONObject
import java.io.Serializable

class Ingredient(data: JSONObject): Serializable {
    val name: String = data["name"] as String? ?: ""
    val weight: Int = data["weight"] as Int? ?: 0

    override fun toString(): String = "$name $weight"
}
