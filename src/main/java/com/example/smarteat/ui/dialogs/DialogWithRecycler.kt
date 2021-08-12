package com.example.smarteat.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.adapters.PointAdapter
import com.google.android.material.button.MaterialButton

class DialogWithRecycler(
    private val header: String = "",
    private val content: Array<String> = Array(0) { "" },
    private val btnText: String = "",
    private val callBack: (View) -> Unit = {}
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_with_recycler, null)

            val headerTextView: TextView = view.findViewById(R.id.dialog_with_recycler__header)
            val contentTextView: RecyclerView = view.findViewById(R.id.dialog_with_recycler__recycler)
            val button: MaterialButton = view.findViewById(R.id.dialog_with_recycler__accept_btn)
            val cross: ImageView = view.findViewById(R.id.dialog_with_recycler__cross)

            headerTextView.text = header
            contentTextView.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            contentTextView.adapter = PointAdapter(content)
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
}