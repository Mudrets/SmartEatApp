package com.example.smarteat.ui.activities

import android.content.Intent
import android.graphics.drawable.Drawable
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
import com.example.smarteat.ui.dialogs.DialogWithoutButton
import com.example.smarteat.utils.ReadWriter
import com.example.smarteat.utils.RequestSender
import com.google.android.material.button.MaterialButton
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class RegistrationActivity() : AppCompatActivity() {
    private lateinit var emailField : EditText
    private lateinit var passwordField : EditText
    private lateinit var repeatPasswordField : EditText
    private lateinit var emailWarning : TextView
    private lateinit var passwordWarning : TextView
    private lateinit var normalFieldBg : Drawable
    private lateinit var warningFieldBg : Drawable
    private lateinit var progressBar: ProgressBar
    private lateinit var registrationButton: MaterialButton
    private lateinit var backBtn: MaterialButton
    private val registrationCallBack = { response: JSONObject ->
        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val userDataFile = File(dir, "userData.json")
        if (ReadWriter.writeDataToFile(userDataFile, response.toString())) {
            val intent = Intent(this, MainActivity().javaClass)
            startActivity(intent)
        } else {
            val content = resources.getString(R.string.error_registration)
            val dialog = DialogWithoutButton("Ошибка регистрации", content)
            dialog.show(supportFragmentManager, "registration error")
        }
        progressBar.visibility = ProgressBar.GONE
        setEnabledToAllButtons(true)
    }
    private val errorCallBack: (VolleyError) -> Unit = { error: VolleyError ->
        val content = resources.getString(R.string.error_registration)
        val dialog = DialogWithoutButton("Ошибка регистрации", content)
        dialog.show(supportFragmentManager, "registration error")
        Log.e("Registration error", error.toString())
        progressBar.visibility = ProgressBar.GONE
        setEnabledToAllButtons(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initializeComponents()
        setDefaultFieldSettings()

        emailField.addTextChangedListener(
                { _, _, _, _ -> },
                { _, _, _, _ -> setDefaultFieldSettings()},
                {}
        )
        passwordField.addTextChangedListener(
                { _, _, _, _ -> },
                { _, _, _, _ -> setDefaultFieldSettings()},
                {}
        )
        repeatPasswordField.addTextChangedListener(
                { _, _, _, _ -> },
                { _, _, _, _ -> setDefaultFieldSettings()},
                {}
        )
    }

    private fun initializeComponents() {
        emailField = findViewById(R.id.activity_registration__email_field)
        passwordField = findViewById(R.id.activity_registration__password_field)
        repeatPasswordField = findViewById(R.id.activity_registration__repeat_password_field)
        emailWarning = findViewById(R.id.activity_registration__email_warning)
        passwordWarning = findViewById(R.id.activity_registration__password_warning)
        normalFieldBg = ResourcesCompat.getDrawable(this.resources, R.drawable.authorization_edit_text_border, null)!!
        warningFieldBg = ResourcesCompat.getDrawable(resources, R.drawable.authorization_edit_text_border_red, null)!!
        progressBar = findViewById(R.id.activity_registration__progress_bar)
        registrationButton = findViewById(R.id.activity_registration__registration_btn)
        backBtn = findViewById(R.id.activity_registration__back_btn)
    }

    private fun setEnabledToAllButtons(value: Boolean) {
        registrationButton.isEnabled = value
        backBtn.isEnabled = value
    }

    private fun checkEmailAndSendSignUpRequest(email: String, password: String, repeat: String) {
        progressBar.visibility = ProgressBar.VISIBLE
        setEnabledToAllButtons(false)
        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val recipesDataFile = File(dir, "recipesData.json")
        val ingredientsDataFile = File(dir, "ingredientsData.json")

        if (recipesDataFile.exists() && ingredientsDataFile.exists()) {
            val emailJsonObj = JSONObject("{email: \"$email\"}")

            val request = RequestSender.getJsonRequestStringResponse(
                RequestSender.CHECK_EMAIL_URL,
                emailJsonObj,
                { response: String ->
                    if (response != "1")
                        sendSignUpRequest(email, password, repeat)
                    else {
                        emailField.background = warningFieldBg
                        emailWarning.text = "Пользователь с таким email уже существует"
                    }
                    progressBar.visibility = ProgressBar.GONE
                    setEnabledToAllButtons(true)
                },
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

    private fun sendSignUpRequest(email: String, password: String, repeat: String) {
        progressBar.visibility = ProgressBar.VISIBLE
        setEnabledToAllButtons(false)
        val userId = UUID.randomUUID()

        val params: MutableMap<Any?, Any?> = HashMap()
        //Set data
        params["email"] = email
        params["password"] = password
        params["repeat"] = repeat
        params["type"] = "personal"
        params["userId"] = userId.toString()

        val jsonObj = JSONObject(params) //Create json for request
        val userJsonObj = JSONObject("{user: {}}")
        userJsonObj.put("user", jsonObj)

        val request = RequestSender.getJsonRequestJsonResponse(
            RequestSender.REGISTER_URL,
            userJsonObj,
            registrationCallBack,
            errorCallBack
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }

    private fun setDefaultFieldSettings() {
        emailField.background = normalFieldBg
        passwordField.background = normalFieldBg
        repeatPasswordField.background = normalFieldBg
        emailWarning.text = ""
        passwordWarning.text = ""
    }

    fun onClickBackButton(view: View) {
        finish()
    }

    private fun EditText.isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()
    }

    fun onClickField(view: View) {
        emailField.background = normalFieldBg
        repeatPasswordField.background = normalFieldBg
        emailWarning.text = ""
        passwordWarning.text = ""
    }

    private fun checkPassword(pass: String): Int {
        var hasDigits = false
        var hasUpper = false
        var hasLower = false
        if (pass.length < 6)
            return 6
        for (ch in pass) {
            if (ch.isDigit())
                hasDigits = true
            if (ch.isLowerCase())
                hasLower = true
            if (ch.isUpperCase())
                hasUpper = true
            if (ch.isWhitespace())
                return 1
            if (ch in 'а'..'я' || ch in 'А'..'Я')
                return 2
        }
        if (!hasDigits)
            return 3
        if (!hasLower)
            return 4
        if (!hasUpper)
            return 5
        return 0
    }

    fun onClickRegistrationBtn(view: View) {
        emailField.background = normalFieldBg
        repeatPasswordField.background = normalFieldBg
        passwordField.background = normalFieldBg
        emailWarning.text = ""
        passwordWarning.text = ""

        val email = emailField.text.toString()
        val passwordText = passwordField.text.toString()
        val repeatPasswordText = repeatPasswordField.text.toString()

        val code = checkPassword(passwordText)
        if (emailField.isEmailValid() && passwordText == repeatPasswordText && code == 0) {
            checkEmailAndSendSignUpRequest(email, passwordText, repeatPasswordText)
        } else if (!emailField.isEmailValid()) {
            emailField.background = warningFieldBg
            emailWarning.text = "Неверный формат почты"
        } else if (code != 0) {
            passwordWarning.text = when(code) {
                1 -> "Пароль не может содержать пробелов"
                2 -> "Пароль не может кириллицу"
                3 -> "Пароль должен содержать цифры"
                4 -> "Пароль должен содержать строчные буквы"
                5 -> "Пароль должен содеражть прописные буквы"
                6 -> "Пароль должен содержать как минимум 6 символов"
                else -> ""
            }
            passwordField.background = warningFieldBg
        } else if (passwordText != repeatPasswordText) {
            repeatPasswordField.background = warningFieldBg
            passwordWarning.text = "Пароли не совпадают"
        }
    }
}