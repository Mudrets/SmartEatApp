package com.example.smarteat.utils

import android.graphics.drawable.Drawable
import android.view.View

object StyleManager {
    lateinit var authorizationNormalBackground: Drawable
    lateinit var authorizationWarningBackground: Drawable
    lateinit var fieldNormalBackground: Drawable
    lateinit var fieldWarningBackground: Drawable
    lateinit var selectorNormalBackground: Drawable
    lateinit var selectorWarningBackground: Drawable

    fun setBackground(background: Drawable, vararg fields: View) {
        for (field in fields)
            field.background = background
    }
}