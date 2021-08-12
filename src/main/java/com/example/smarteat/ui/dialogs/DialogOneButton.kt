package com.example.smarteat.ui.dialogs

import android.app.Dialog
import android.app.KeyguardManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.smarteat.R
import com.example.smarteat.utils.AchievementChecker
import com.google.android.material.button.MaterialButton

class DialogOneButton(
    private val header: String = "",
    private val content: String = "",
    private val btnText: String = "",
    private val callBack: (View) -> Unit = {},
    private val dismissCallback: () -> Unit = {}
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_with_one_btn, null)

            val headerTextView: TextView = view.findViewById(R.id.dialog_with_one_btn__header)
            val contentTextView: TextView = view.findViewById(R.id.dialog_with_one_btn__content)
            val button: MaterialButton = view.findViewById(R.id.dialog_with_one_btn__accept_btn)
            val cross: ImageView = view.findViewById(R.id.dialog_with_one_btn__cross)

            headerTextView.text = header
            contentTextView.text = content
            button.text = btnText
            button.setOnClickListener {
                callBack(it)
                dismiss()
            }
            cross.setOnClickListener {
                dismiss()
            }

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun dismiss() {
        dismissCallback()
        super.dismiss()
    }
}