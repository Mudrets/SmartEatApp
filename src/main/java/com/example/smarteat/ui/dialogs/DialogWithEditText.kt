package com.example.smarteat.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.smarteat.R
import com.example.smarteat.models.Form
import com.google.android.material.button.MaterialButton

class DialogWithEditText(
    private val form: Form,
    private val header: String = "",
    private val editTextHint: String = "",
    private val acceptBtnText: String = "",
    private val acceptCallBack: (View) -> Unit = {},
    private val cancelCallBack: (View) -> Unit = {}
) : DialogFragment() {
    var resultText: String = ""
        private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_with_edit_text, null)

            val headerTextView: TextView = view.findViewById(R.id.dialog_with_edit_text__header)
            val editText: EditText = view.findViewById(R.id.dialog_with_edit_text__edit_text)
            val acceptButton: MaterialButton = view.findViewById(R.id.dialog_with_edit_text__accept_btn)
            val cross: ImageView = view.findViewById(R.id.dialog_with_edit_text__cross)

            editText.hint = editTextHint
            if (form.hasPlan)
                editText.setText(form.name)

            headerTextView.text = header
            acceptButton.text = acceptBtnText
            acceptButton.setOnClickListener {
                form.name = editText.text.toString().trim()
                acceptCallBack(it)
                dismiss()
            }

            cross.setOnClickListener {
                cancelCallBack(it)
                dismiss()
            }

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}