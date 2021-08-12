package com.example.smarteat.vh

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.ui.fragments.FormsFragment
import com.google.android.material.button.MaterialButton

class BtnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val btn: MaterialButton = itemView.findViewById(R.id.new_form_button__add_form_button)
}