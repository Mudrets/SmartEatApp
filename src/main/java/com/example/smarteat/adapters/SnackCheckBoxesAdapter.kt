package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Form
import com.example.smarteat.ui.fragments.CountOfSnacks
import com.example.smarteat.utils.RecipesIngredientsInfo

class SnackCheckBoxesAdapter(
    val snacks: ArrayList<String>,
    val form: Form
) : RecyclerView.Adapter<SnackCheckBoxesAdapter.CheckBoxesViewHolder>() {
    inner class CheckBoxesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_row__checkbox)
        init {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val value = checkBox.text.toString()
                val index = snacks.indexOf(value);
                form.snacks[index] = isChecked
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.checkbox_row

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckBoxesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return CheckBoxesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckBoxesViewHolder, position: Int) {
        holder.checkBox.text = snacks[position]
        holder.checkBox.isChecked = form.snacks[position]
    }

    override fun getItemCount(): Int = snacks.size
}