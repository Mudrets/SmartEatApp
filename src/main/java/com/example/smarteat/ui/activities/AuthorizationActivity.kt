package com.example.smarteat.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.example.smarteat.R
import com.example.smarteat.models.Recipe
import com.example.smarteat.ui.dialogs.DialogWithoutButton
import com.example.smarteat.utils.*
import com.google.android.material.button.MaterialButton
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.File

class AuthorizationActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var warning: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var enterBtn: MaterialButton
    private lateinit var registrationBtn: MaterialButton
    private lateinit var forgotPasswordBtn: MaterialButton
    private val responseCallBack = { response: JSONObject ->
        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val userDataFile = File(dir, "userData.json")
        response.put("withExtra", false)
        if (ReadWriter.writeDataToFile(userDataFile, response.toString())) {
            val intent = Intent(this, MainActivity().javaClass)
            startActivity(intent)
        } else {
            val content = resources.getString(R.string.error_authorization)
            val dialog = DialogWithoutButton("Ошибка авторизации", content)
            dialog.show(supportFragmentManager, "authorization error")
        }
        progressBar.visibility = ProgressBar.GONE
        setAllButtonsEnabled(true)
    }
    private val fetchBootstrapResponseCallBack = { response: JSONObject ->
        val ingredientsInfoJson = response.getJSONArray("ingredients")
        val recipesInfoJson = response.getJSONArray("recipes")

        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val recipesDataFile = File(dir, "recipesData.json")
        val ingredientsDataFile = File(dir, "ingredientsData.json")

        if (!ReadWriter.writeDataToFile(recipesDataFile, recipesInfoJson.toString()) ||
            !ReadWriter.writeDataToFile(ingredientsDataFile, ingredientsInfoJson.toString())
        ) {
            val content = resources.getString(R.string.error_download_files_with_info)
            val dialog = DialogWithoutButton("Ошибка", content)
            dialog.show(supportFragmentManager, "server error")
        }
    }
    private val errorCallBack: (VolleyError) -> Unit = { error: VolleyError ->
        if (error.networkResponse?.statusCode == 404)
            showMsgAboutWrongPasswordOrEmail()
        else {
            val content = resources.getString(R.string.error_authorization)
            val dialog = DialogWithoutButton("Ошибка авторизации", content)
            dialog.show(supportFragmentManager, "authorization error")
            Log.e("Authorization error", error.toString())
        }
        progressBar.visibility = ProgressBar.GONE
        setAllButtonsEnabled(true)
    }
    private val errorFetchBootstrapCallBack: (VolleyError) -> Unit = { error: VolleyError ->
        val content = resources.getString(R.string.error_download_files_with_info)
        val dialog = DialogWithoutButton("Ошибка", content)
        dialog.show(supportFragmentManager, "server error")
        Log.e("Server error", error.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val dataFile = File(dir, "userData.json")

        initializeComponents()
        initializeStyleManager()
        parseRecipes()

//        dataFile.delete()
        if (!dataFile.exists()) {
            setDefaultFieldSettings()

            emailField.addTextChangedListener(
                { _, _, _, _ -> },
                { _, _, _, _ -> setDefaultFieldSettings() },
                {}
            )
            passwordField.addTextChangedListener(
                { _, _, _, _ -> },
                { _, _, _, _ -> setDefaultFieldSettings() },
                {}
            )

            sendFetchBootstrapRequest()
        } else {
            val intent = Intent(this, MainActivity().javaClass)
            startActivity(intent)
        }
    }

    private fun setAllButtonsEnabled(value: Boolean) {
        enterBtn.isEnabled = value
        registrationBtn.isEnabled = value
        forgotPasswordBtn.isEnabled = value
    }

    private fun initializeStyleManager() {
        StyleManager.authorizationNormalBackground =
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.authorization_edit_text_border,
                null
            )!!
        StyleManager.authorizationWarningBackground =
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.authorization_edit_text_border_red,
                null
            )!!
        StyleManager.fieldNormalBackground =
            ResourcesCompat.getDrawable(resources, R.drawable.field_edit_text_border, null)!!
        StyleManager.fieldWarningBackground =
            ResourcesCompat.getDrawable(resources, R.drawable.field_edit_text_border_red, null)!!
        StyleManager.selectorNormalBackground =
            ResourcesCompat.getDrawable(resources, R.drawable.selector_edit_text_border, null)!!
        StyleManager.selectorWarningBackground =
            ResourcesCompat.getDrawable(resources, R.drawable.selector_edit_text_border_red, null)!!
    }

    private fun parseRecipes() {
        val sections = Jsoup.parse(resources.assets.open("html/CookBookFull.html").bufferedReader().use {
            it.readText()
        }).select("section")
        for (section in sections) {
            val recipe = HtmlParser.htmlToRecipe(section)
            RecipesIngredientsInfo.recipes[recipe.id] = recipe
        }
    }

    private fun initializeComponents() {
        emailField = findViewById(R.id.activity_authorization__email_field)
        passwordField = findViewById(R.id.activity_authorization__password_field)
        warning = findViewById(R.id.activity_authorization__password_warning)
        progressBar = findViewById(R.id.activity_authorization__progress_bar)
        enterBtn = findViewById(R.id.activity_authorization__enter_btn)
        registrationBtn = findViewById(R.id.activity_authorization__registration_btn)
        forgotPasswordBtn = findViewById(R.id.activity_authorization__forgot_password)
    }

    private fun setDefaultFieldSettings() {
        emailField.background = StyleManager.authorizationNormalBackground
        passwordField.background = StyleManager.authorizationNormalBackground
        warning.text = ""
    }

    fun onClickRegisterButton(view: View) {
        val intent = Intent(this, RegistrationActivity().javaClass)
        startActivity(intent)
    }

    fun onClickForgotPasswordButton(view: View) {
        val intent = Intent(this, ForgotPasswordActivity().javaClass)
        startActivity(intent)
    }

    private fun EditText.isEmailValid(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString().trim()).matches()

    private fun showMsgAboutWrongPasswordOrEmail() {
        passwordField.background = StyleManager.authorizationWarningBackground
        warning.text = "Неверный логин и/или пароль"
    }

    fun onClickField(view: View) {
        passwordField.background = StyleManager.authorizationNormalBackground
        warning.text = ""
    }

    private fun sendSignInRequest(email: String, password: String) {
        progressBar.visibility = ProgressBar.VISIBLE
        setAllButtonsEnabled(false)
        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val recipesDataFile = File(dir, "recipesData.json")
        val ingredientsDataFile = File(dir, "ingredientsData.json")

        if (recipesDataFile.exists() && ingredientsDataFile.exists()) {
            val params: MutableMap<Any?, Any?> = HashMap()
            //Set data
            params["password"] = password
            params["email"] = email
            params["remember"] = true

            val jsonObj = JSONObject(params) //Create json for request
            val request = RequestSender.getJsonRequestJsonResponse(
                RequestSender.SINGIN_URL,
                jsonObj,
                responseCallBack,
                errorCallBack
            )

            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(request)
        } else {
            val content = resources.getString(R.string.error_download_files_with_info)
            val dialog = DialogWithoutButton("Ошибка", content)
            dialog.show(supportFragmentManager, "server error")
        }
    }

    private fun sendFetchBootstrapRequest() {
        val params: MutableMap<Any?, Any?> = HashMap()
        //Set data
        params["pathname"] = "/app/in"

        val jsonObj = JSONObject(params) //Create json for request
        val request = RequestSender.getJsonRequestJsonResponse(
            RequestSender.FETCH_BOOTSTRAP_URL,
            jsonObj,
            fetchBootstrapResponseCallBack,
            errorFetchBootstrapCallBack
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }

    fun onClickEnterButton(view: View) {
        val userPass = passwordField.text.toString()
        val userEmail = emailField.text.toString().trim()

        if (!emailField.isEmailValid())
            showMsgAboutWrongPasswordOrEmail()
        else
            sendSignInRequest(userEmail, userPass)
    }
}