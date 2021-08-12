package com.example.smarteat.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smarteat.R
import com.google.android.material.button.MaterialButton

class AboutUsActivity : AppCompatActivity() {
    private lateinit var backButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        backButton = findViewById(R.id.activity_about_us__back_btn)
        backButton.setOnClickListener {
            finish()
        }
    }
}