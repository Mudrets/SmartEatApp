package com.example.smarteat.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.smarteat.R
import com.example.smarteat.models.User
import com.example.smarteat.utils.AchievementChecker
import com.example.smarteat.utils.StyleManager
import com.google.android.material.button.MaterialButton

class EditProfileActivity(val mainActivity: MainActivity? = null) : AppCompatActivity() {
    private lateinit var fullNameField: EditText
    private lateinit var telephoneField: EditText
    private lateinit var warningMsgTextView: TextView
    private lateinit var saveButton: MaterialButton
    private lateinit var backBtn: MaterialButton
    private var user: User? = AchievementChecker.user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        fullNameField = findViewById(R.id.activity_edit_profile__edit_name_and_surname)
        telephoneField = findViewById(R.id.activity_edit_profile__telephone)
        warningMsgTextView = findViewById(R.id.activity_edit_profile__warning_msg_telephone)
        saveButton = findViewById(R.id.activity_edit_profile__save_btn)
        backBtn = findViewById(R.id.activity_edit_profile__back_btn)
        telephoneField.background = StyleManager.authorizationNormalBackground
        warningMsgTextView.text = ""
        fullNameField.setText(user?.fullName ?: "")
        telephoneField.setText(user?.phone ?: "")

        saveButton.setOnClickListener {
            telephoneField.background = StyleManager.authorizationNormalBackground
            fullNameField.background = StyleManager.authorizationNormalBackground
            warningMsgTextView.text = ""
            val fullName = fullNameField.text.toString().trim()
            val telephone = telephoneField.text.toString().trim()
            val code = checkFullName(fullName)
            if (code != 0) {
                fullNameField.background = StyleManager.authorizationWarningBackground
                if (code == 1)
                    warningMsgTextView.text = "Полное имя может содержать не более 4 слов"
                else
                    warningMsgTextView.text = "Имя не может содержать цифры"
            } else if (!android.util.Patterns.PHONE.matcher(telephone).matches()) {
                telephoneField.background = StyleManager.authorizationWarningBackground
                warningMsgTextView.text = "Неверный формат мобильного телефона"
            } else {
                if (user != null) {
                    user!!.fullName = fullName
                    user!!.phone = telephone
                    mainActivity?.profileFragment?.username?.text = fullName
                }
                finish()
            }
        }

        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun checkFullName(name: String): Int {
        for (ch in name)
            if (ch.isDigit())
                return 2
        return if (name.split(" ").size <= 4) 0 else 1
    }

}