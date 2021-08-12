package com.example.smarteat.utils

import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.size

object FieldsChecker {

    fun isFieldHasNum(textField: EditText, from: Int, to: Int): Boolean {
        return if (textField.text.toString().toIntOrNull() == null)
            false
        else
            textField.text.toString().toInt() in from..to
    }

    fun checkAllFields(
        checker: (textField: EditText) -> Boolean,
        vararg textFields: EditText
    ): ArrayList<EditText> {
        val res = ArrayList<EditText>()
        for (textField in textFields) {
            if (!checker(textField))
                res.add(textField)
        }
        return res
    }

    fun checkAllFields(
        checker: (textField: EditText, val1: Int, val2: Int) -> Boolean,
        val1: Array<Int>,
        val2: Array<Int>,
        vararg textFields: EditText
    ): ArrayList<EditText> {
        val res = ArrayList<EditText>()
        for ((counter, textField) in textFields.withIndex()) {
            if (!checker(textField, val1[counter], val2[counter]))
                res.add(textField)
        }
        return res
    }

    fun checkAllSpinners(vararg spinners: Spinner): ArrayList<Spinner> {
        val res = ArrayList<Spinner>()
        for (spinner in spinners) {
            if (spinner.selectedItemPosition == spinner.adapter.count)
                res.add(spinner)
        }
        return res
    }
}