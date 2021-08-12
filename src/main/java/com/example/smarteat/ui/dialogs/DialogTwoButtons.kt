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

class DialogTwoButtons(
    private val header: String = "",
    private val content: String = "",
    private val acceptBtnText: String = "",
    private val cancelBtnText: String = "",
    private val acceptCallBack: (View) -> Unit = {},
    private val cancelCallBack: (View) -> Unit = {}
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_with_two_btn, null)

            val headerTextView: TextView = view.findViewById(R.id.dialog_with_two_btn__header)
            val contentTextView: TextView = view.findViewById(R.id.dialog_with_two_btn__content)
            val acceptButton: MaterialButton = view.findViewById(R.id.dialog_with_two_btn__accept_btn)
            val cancelButton: MaterialButton = view.findViewById(R.id.dialog_with_two_btn__cancel_btn)
            val cross: ImageView = view.findViewById(R.id.dialog_with_two_btn__cross)

            headerTextView.text = header
            contentTextView.text = content
            acceptButton.text = acceptBtnText
            acceptButton.setOnClickListener {
                acceptCallBack(it)
                dismiss()
            }
            cancelButton.text = cancelBtnText
            cancelButton.setOnClickListener {
                cancelCallBack(it)
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