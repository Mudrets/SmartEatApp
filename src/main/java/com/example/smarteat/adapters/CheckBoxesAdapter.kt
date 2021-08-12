package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Form
import com.example.smarteat.ui.fragments.QuestionsFragment
import com.example.smarteat.utils.RecipesIngredientsInfo

class CheckBoxesAdapter(
    private val products: ArrayList<String>,
    private val form: Form,
    private val parent: QuestionsFragment
) : RecyclerView.Adapter<CheckBoxesAdapter.CheckBoxesViewHolder>() {

    inner class CheckBoxesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_row__checkbox)
        init {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val value = checkBox.text.toString()
                val name = RecipesIngredientsInfo.quizNameToName[value] ?: ""
                if (isChecked) {
                    if (name != "" && !form.ingredients.contains(name))
                        form.ingredients.add(name)
                } else
                    form.ingredients.remove(name)
                parent.updateWarningMessage()
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
        holder.checkBox.text = products[position]
        holder.checkBox.isChecked = form.ingredients.contains(RecipesIngredientsInfo.quizNameToName[products[position]])
    }

    override fun getItemCount(): Int = products.size

}