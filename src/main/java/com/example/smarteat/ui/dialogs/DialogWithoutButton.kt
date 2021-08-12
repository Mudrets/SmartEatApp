package com.example.smarteat.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.smarteat.R
import com.google.android.material.button.MaterialButton

class DialogWithoutButton(
    private val header: String = "",
    private val content: String = ""
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_without_btn, null)

            val headerTextView: TextView = view.findViewById(R.id.dialog_without_btn__header)
            val contentTextView: TextView = view.findViewById(R.id.dialog_without_btn__content)
            val cross: ImageView = view.findViewById(R.id.dialog_without_btn__cross)

            headerTextView.text = header
            contentTextView.text = content
            cross.setOnClickListener {
                dismiss()
            }

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}