package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Form
import com.example.smarteat.ui.dialogs.BottomFormDialog
import com.example.smarteat.ui.fragments.FormsFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NotCompleteFormsAdapter(
    private val notCompleteForms: ArrayList<Form>,
    private val parentFragment: FormsFragment
): RecyclerView.Adapter<NotCompleteFormsAdapter.NotCompleteFormsViewHolder>() {

    inner class NotCompleteFormsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.not_complete_form_row__form_name)
        val date: TextView = itemView.findViewById(R.id.not_complete_form_row__date)
        val cardViewHolder: CardView = itemView.findViewById(R.id.not_complete_form_row__card)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.not_complete_form_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotCompleteFormsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return NotCompleteFormsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotCompleteFormsViewHolder, position: Int) {
        val form = notCompleteForms[position]
        holder.name.text = form.name
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        holder.date.text = format.format(form.updatedAt)
        holder.cardViewHolder.setOnClickListener {
            val dialog = BottomFormDialog(form)
            parentFragment.showBottomDialogSheet(dialog)
        }
    }

    override fun getItemCount(): Int = notCompleteForms.size
}