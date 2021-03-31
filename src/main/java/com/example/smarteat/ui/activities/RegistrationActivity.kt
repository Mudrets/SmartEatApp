package com.example.smarteat.ui.activities

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.example.smarteat.R

class RegistrationActivity : AppCompatActivity() {
    private lateinit var emailField : EditText
    private lateinit var passwordField : EditText
    private lateinit var repeatPasswordField : EditText
    private lateinit var emailWarning : TextView
    private lateinit var passwordWarning : TextView
    private lateinit var normalFieldBg : Drawable
    private lateinit var warningFieldBg : Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        //initialization
        emailField = findViewById(R.id.activity_registration__email_field)
        passwordField = findViewById(R.id.activity_registration__password_field)
        repeatPasswordField = findViewById(R.id.activity_registration__repeat_password_field)
        emailWarning = findViewById(R.id.activity_registration__email_warning)
        passwordWarning = findViewById(R.id.activity_registration__password_warning)
        normalFieldBg = ResourcesCompat.getDrawable(this.resources, R.drawable.edit_text_border, null)!!
        warningFieldBg = ResourcesCompat.getDrawable(resources, R.drawable.edit_text_border_red, null)!!

        emailField.background = normalFieldBg
        passwordField.background = normalFieldBg
        repeatPasswordField.background = normalFieldBg
        emailWarning.text = ""
        passwordWarning.text = ""

        emailField.addTextChangedListener(
                {text, n1, n2, n3 -> },
                {text, n1, n2, n3 -> setDefaultFieldSettings()},
                {text ->  }
        )
        passwordField.addTextChangedListener(
                {text, n1, n2, n3 -> },
                {text, n1, n2, n3 -> setDefaultFieldSettings()},
                {text ->  }
        )
        repeatPasswordField.addTextChangedListener(
                {text, n1, n2, n3 -> },
                {text, n1, n2, n3 -> setDefaultFieldSettings()},
                {text ->  }
        )
    }

    private fun setDefaultFieldSettings() {
        repeatPasswordField.background = normalFieldBg
        emailField.background = normalFieldBg
        emailWarning.text = ""
        passwordWarning.text = ""
    }

    fun onClickBackButton(view: View) {
        finish();
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

    fun onClickRegistrationBtn(view: View) {
        emailField.background = normalFieldBg
        repeatPasswordField.background = normalFieldBg
        emailWarning.text = ""
        passwordWarning.text = ""
        val passwordText = passwordField.text.toString()
        val repeatPasswordText = repeatPasswordField.text.toString()

        if (emailField.isEmailValid() && passwordText == repeatPasswordText) {
            val myToast = Toast.makeText(this, "Successful registration", Toast.LENGTH_SHORT)
            myToast.show()
        } else if (!emailField.isEmailValid()) {
            emailField.background = warningFieldBg
            emailWarning.text = "Неверный формат почты"
        } else if (passwordText != repeatPasswordText) {
            repeatPasswordField.background = warningFieldBg
            passwordWarning.text = "Пароли не совпадают"
        }
        //TODO: проверка наличия почты в БД
    }
}