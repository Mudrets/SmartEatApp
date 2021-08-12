package com.example.smarteat.ui.dialogs

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.smarteat.R

class DialogImage(
    private val image: Drawable,
    private val header: String = "",
    private val subtitle: String = "",
    private val content: String = ""
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_with_image, null)

            val headerTextView: TextView = view.findViewById(R.id.dialog_with_image__header)
            val contentTextView: TextView = view.findViewById(R.id.dialog_with_image__content)
            val cross: ImageView = view.findViewById(R.id.dialog_with_image__cross)
            val subtitleTextView: TextView = view.findViewById(R.id.dialog_with_image__subtitle)
            val imageView: ImageView = view.findViewById(R.id.dialog_with_image__image)

            headerTextView.text = header
            contentTextView.text = content
            subtitleTextView.text = subtitle
            imageView.setImageDrawable(image)
            cross.setOnClickListener {
                dismiss()
            }

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}