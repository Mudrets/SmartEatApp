package com.example.smarteat.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RequestSender(_context: Context, _url: String){
    private val context = _context
    private var url: String = _url
    private var jsonResponse: JSONObject? = JSONObject()
    private var error: VolleyError? = VolleyError()

    public fun getJsonResponse(): JSONObject? {
        return jsonResponse
    }

    public fun getError(): VolleyError? {
        return error
    }

    public fun sendSignInRequest(password: String, email: String) {
        jsonResponse = null
        error = null

        val params: MutableMap<Any?, Any?> = HashMap()
        //Set data
        params["email"] = email
        params["password"] = password
        params["remember"] = true

        val jsonObj = JSONObject(params) //Create json for request
        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonObj,
                { response ->
                    jsonResponse = response
                    System.console().printf(response.toString())
                },
                { error ->
                    this.error = error
                    System.console().printf(error.toString())
                }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(jsonRequest)
    }
}