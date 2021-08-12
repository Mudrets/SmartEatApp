package com.example.smarteat.vh

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.User

class WarningMsgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val crossBtn: ImageView = itemView.findViewById(R.id.warning_msg_row__cross_btn)
    val msg: TextView = itemView.findViewById(R.id.warning_msg_row__warning_text)
    val layout: ViewGroup = itemView.findViewById(R.id.warning_msg_row__warning_message_block)
    var user: User? = null

    init {
        crossBtn.setOnClickListener {
            layout.visibility = View.GONE
            user?.showFormsWarnings = false
        }
    }
}