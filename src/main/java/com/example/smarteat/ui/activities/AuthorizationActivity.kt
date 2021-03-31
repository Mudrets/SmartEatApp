package com.example.smarteat.ui.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smarteat.R
import com.example.smarteat.models.User
import com.example.smarteat.utils.RequestSender
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.Exception

class AuthorizationActivity : AppCompatActivity() {
    private lateinit var emailField : EditText
    private lateinit var passwordField : EditText
    private lateinit var warning : TextView
    private lateinit var normalFieldBg : Drawable
    private lateinit var warningFieldBg : Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val dataFile = File(dir, "userData.json")
        if (!dataFile.exists()) {
            //initialization
            emailField = findViewById(R.id.activity_authorization__email_field)
            passwordField = findViewById(R.id.activity_authorization__password_field)
            warning = findViewById(R.id.activity_authorization__password_warning)
            normalFieldBg =
                ResourcesCompat.getDrawable(this.resources, R.drawable.edit_text_border, null)!!
            warningFieldBg =
                ResourcesCompat.getDrawable(resources, R.drawable.edit_text_border_red, null)!!

            passwordField.background = normalFieldBg
            emailField.background = normalFieldBg
            warning.text = ""

            emailField.addTextChangedListener(
                { text, n1, n2, n3 -> },
                { text, n1, n2, n3 -> setDefaultFieldSettings() },
                { text -> }
            )
            passwordField.addTextChangedListener(
                { text, n1, n2, n3 -> },
                { text, n1, n2, n3 -> setDefaultFieldSettings() },
                { text -> }
            )
        } else {
            val intent = Intent(this, MainActivity().javaClass)
            startActivity(intent)
        }
    }

    private fun setDefaultFieldSettings() {
        passwordField.background = normalFieldBg
        emailField.background = normalFieldBg
        warning.text = ""
    }

    fun onClickRegisterButton(view: View) {
        val intent = Intent(this, RegistrationActivity().javaClass)
        startActivity(intent)
    }

    private fun EditText.isEmailValid(): Boolean =
            android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()

    private fun showMsgAboutWrongPasswordOrEmail() {
        passwordField.background = warningFieldBg
        warning.text = "Неверный логин и/или пароль"
    }

    /**
     * Write info about user to userData.json in directoryDocuments
     */
    private fun writeUserData(output: String, content: String) : Boolean {
        return try {
            val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val outputFile = File(dir, output)
            if (!outputFile.exists())
                outputFile.createNewFile()
            outputFile.writeText(content)
            true
        } catch (ex: IOException) {
            false
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

    fun onClickField(view: View) {
        passwordField.background = normalFieldBg
        warning.text = ""
    }

    private fun sendSignInRequest(email: String, password: String) {
        val params: MutableMap<Any?, Any?> = HashMap()
        //Set data
        params["email"] = email
        params["password"] = password
        params["remember"] = true

        val url = "http://10.0.2.2:8084/api/v0.1/signIn"

        val jsonObj = JSONObject(params) //Create json for request
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonObj,
            { response ->
                if (writeUserData("userData.json", response.toString())) {
                    val intent = Intent(this, MainActivity().javaClass)
                    startActivity(intent)
                } else
                    Toast.makeText(this, R.string.error_authorization,
                        Toast.LENGTH_LONG).show()
            },
            { error ->
                if (error?.networkResponse?.statusCode == 404)
                    showMsgAboutWrongPasswordOrEmail()
                else {
                    Toast.makeText(this, R.string.error_authorization,
                        Toast.LENGTH_LONG).show()
                    System.console().printf(error?.networkResponse.toString())
                }
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonRequest)
    }

    fun onClickEnterButton(view: View) {
        val userPass = passwordField.text.toString()
        val userEmail = emailField.text.toString()

        if (!emailField.isEmailValid())
            showMsgAboutWrongPasswordOrEmail()
        else {
            sendSignInRequest(userEmail, userPass)
        }

    }
}