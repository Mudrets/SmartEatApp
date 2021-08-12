package com.example.smarteat.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.example.smarteat.R
import com.example.smarteat.ui.dialogs.DialogOneButton
import com.example.smarteat.utils.RequestSender
import com.example.smarteat.utils.StyleManager
import com.google.android.material.button.MaterialButton
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var warningMsg: TextView
    private lateinit var errorMsg: TextView
    private lateinit var resetBtn: MaterialButton
    private lateinit var backBtn: MaterialButton
    private lateinit var progressBar: ProgressBar
    private val responseCallback = { response: String ->
        if (response == "")
            emailError("Пользователя с данной почтой не существует")
        else {
            val dialog = DialogOneButton(
                "Сброс пароля",
                "На почту $response было отправленно письмо с ссылкой для сброса пароля. Для завершения сброса пароля пройдите по этой ссылке и укажите новый пароль",
                "Продолжить"
            )
            dialog.show(supportFragmentManager, "reset password")
        }
        setEnabledToAllButtons(true)
        progressBar.visibility = ProgressBar.GONE
    }
    private val errorCallback = { error: VolleyError ->
        val dialog = DialogOneButton(
            "Ошибка сброса пароля",
            "Во время сброса пароля произошла ошибка, пожалуйста проверьте свое интернет соединение и повторите попытку позже",
            "Продолжить"
        )
        dialog.show(supportFragmentManager, "reset password")
        setEnabledToAllButtons(true)
        progressBar.visibility = ProgressBar.GONE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        emailField = findViewById(R.id.activity_forgot_password__email_field)
        warningMsg = findViewById(R.id.activity_forgot_password__warning_text)
        errorMsg = findViewById(R.id.activity_forgot_password__email_warning)
        resetBtn = findViewById(R.id.activity_forgot_password__reset_btn)
        backBtn = findViewById(R.id.activity_forgot_password__back_btn)
        progressBar = findViewById(R.id.activity_forgot_password__progress_bar)
    }

    private fun setEnabledToAllButtons(value: Boolean) {
        resetBtn.isEnabled = value
        backBtn.isEnabled = value
    }

    fun backBtnOnClickListener(view: View) {
        finish()
    }

    private fun emailError(errorText: String) {
        emailField.background = StyleManager.authorizationWarningBackground
        errorMsg.text = errorText
    }

    private fun EditText.isEmailValid(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString().trim()).matches()

    private fun sendResetPassRequest(email: String) {
        setEnabledToAllButtons(false)
        progressBar.visibility = ProgressBar.VISIBLE
        val params: MutableMap<Any?, Any?> = HashMap()
        params["email"] = email

        val jsonObj = JSONObject(params) //Create json for request
        val request = RequestSender.getJsonRequestStringResponse(
            RequestSender.RESET_PASSWORD_URL,
            jsonObj,
            responseCallback,
            errorCallback
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }

    fun resetBtnOnClickListener(view: View) {
        errorMsg.text = ""
        emailField.background = StyleManager.authorizationNormalBackground

        val email: String = emailField.text.toString().trim()
        if (emailField.isEmailValid())
            sendResetPassRequest(email)
        else
            emailError("Неверный формат электронной почты")
    }

}