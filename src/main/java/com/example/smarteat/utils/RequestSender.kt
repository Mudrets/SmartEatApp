package com.example.smarteat.utils

import android.content.Intent
import android.os.Environment
import android.widget.Toast
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.example.smarteat.R
import com.example.smarteat.ui.activities.MainActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset
//devserver: https://dev.smart-eat.ru/
//server: https://smart-eat.ru/
//localhost: http://10.0.2.2:8084/

object RequestSender {
    const val SERVER = "http://10.0.2.2:8084/"
    const val RESET_PASSWORD_URL = "${SERVER}api/v0.1/resetPassword"
    const val SINGIN_URL = "${SERVER}api/v0.1/signIn"
    const val REGISTER_URL = "${SERVER}api/v0.1/register"
    const val FETCH_BOOTSTRAP_URL = "${SERVER}api/v0.1/fetchBootstrap"
    const val CHECK_EMAIL_URL = "${SERVER}api/v0.1/isEmailBusy"
    const val CREATE_PLAN_URL = "${SERVER}api/v0.1/createPlanMobile"
    const val GET_CPFC_URL = "${SERVER}api/v0.1/calculateTargetPFCK"

    fun getJsonRequestJsonResponse(
        url: String,
        params: JSONObject,
        reaction: (response: JSONObject) -> Unit,
        errorReaction: (error: VolleyError) -> Unit
    ): JsonObjectRequest {
        return object : JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                reaction(response)
            },
            { error ->
                errorReaction(error)
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
    }

    fun getJsonRequestStringResponse(
        url: String,
        params: JSONObject,
        reaction: (response: String) -> Unit,
        errorReaction: (error: VolleyError) -> Unit
    ): JsonRequest<String> {
        return object : JsonRequest<String>(
            Request.Method.POST, url, params.toString(),
            { response: String ->
                reaction(response)
            },
            { error ->
                errorReaction(error)
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json"
                return headers
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                val str = String(
                    response?.data ?: ByteArray(0),
                    Charset.forName(HttpHeaderParser.parseCharset(response?.headers))
                )
                return Response.success(str, HttpHeaderParser.parseCacheHeaders(response))
            }
        }
    }
}