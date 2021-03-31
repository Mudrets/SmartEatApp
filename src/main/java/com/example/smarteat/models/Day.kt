package com.example.smarteat.models

import org.json.JSONObject
import java.io.Serializable

class Day(data: JSONObject): Serializable {
    val eating: ArrayList<Eating> = ArrayList()
    init {
        val gettingArr = ArrayList<Eating>()
        val indexes : Array<Int> = arrayOf(0, 2, 1, 3, 5, 4)
        for (key in data.keys())
            gettingArr.add(Eating(data.getJSONObject(key), key))
        for (index in indexes)
            eating.add(gettingArr[index])
    }

    var isExpanded: Boolean = false
        internal set
}
