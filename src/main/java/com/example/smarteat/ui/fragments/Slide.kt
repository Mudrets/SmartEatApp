package com.example.smarteat.ui.fragments

import androidx.fragment.app.Fragment

interface Slide {
    var prevFragment: Fragment?
    var nextFragment: Fragment?
    fun goToPrev()
    fun goToNext()
    fun setNext(fragment: Fragment?)
    fun setPrev(fragment: Fragment?)
}