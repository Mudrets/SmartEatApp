package com.example.smarteat.models

import android.graphics.drawable.Drawable

data class Achievement(
    internal val name: String,
    internal val description: String,
    internal val image: Drawable,
    internal val isComplete: () -> Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (other is Achievement)
            return name == other.name && description == other.description
        return super.equals(other)
    }
}