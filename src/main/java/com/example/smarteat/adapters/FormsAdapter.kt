package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Form
import com.example.smarteat.models.User
import com.example.smarteat.ui.dialogs.BottomFormDialog
import com.example.smarteat.ui.fragments.FormsFragment
import com.example.smarteat.vh.BtnViewHolder
import com.example.smarteat.vh.WarningMsgViewHolder
import java.time.format.DateTimeFormatter

class FormsAdapter(
    private val user: User,
    internal var warningMsg: String = "",
    private val parentFragment: FormsFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val hasActiveForm: Boolean
        get() = user.activePlan != null

    fun notifyInsertNewPlan() {
        notifyItemInserted(getIndexOfBtn() + 1)
    }

    private fun getIndexOfActiveForm(): Int {
        return if (hasActiveForm && warningMsg != "")
            1
        else if (hasActiveForm || warningMsg != "")
            0
        else
            -1
    }

    private fun getIndexOfBtn(): Int {
        return when {
            hasActiveForm -> getIndexOfActiveForm() + 1
            warningMsg != "" -> 1
            else -> 0
        }
    }

    inner class ActiveFormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.active_form_row__form_name)
        val date: TextView = itemView.findViewById(R.id.active_form_row__date)
        val cardView: View = itemView.findViewById(R.id.active_form_row__card)
    }

    inner class NotActiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.not_active_form_row__form_name)
        val date: TextView = itemView.findViewById(R.id.not_active_form_row__date)
        val cardView: View = itemView.findViewById(R.id.not_active_form_row__card)
    }

    override fun getItemViewType(position: Int): Int {
        return if (warningMsg != "" && position == 0)
            R.layout.warning_msg_row
        else if (hasActiveForm && position == getIndexOfActiveForm())
            R.layout.active_form_row
        else if (position == getIndexOfActiveForm() + 1)
            R.layout.button_row
        else
            R.layout.not_active_from_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.warning_msg_row -> WarningMsgViewHolder(view)
            R.layout.active_form_row -> ActiveFormViewHolder(view)
            R.layout.button_row -> BtnViewHolder(view)
            else -> NotActiveViewHolder(view)
        }
    }

    private fun onBindActiveFormViewHolder(holder: ActiveFormViewHolder, form: Form) {
        holder.name.text = form.name
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        holder.date.text = format.format(form.updatedAt)
        holder.cardView.setOnClickListener {
            val dialog = BottomFormDialog(form)
            parentFragment.showBottomDialogSheet(dialog)
        }
    }

    private fun onBindNotActiveFormViewHolder(holder: NotActiveViewHolder, form: Form) {
        holder.name.text = form.name
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        holder.date.text = format.format(form.updatedAt)
        holder.cardView.setOnClickListener {
            val dialog = BottomFormDialog(form)
            parentFragment.showBottomDialogSheet(dialog)
        }
    }

    private fun onBindBtnViewHolder(holder: BtnViewHolder) {
        holder.btn.text = "Новая анкета"
        holder.btn.setOnClickListener {
            parentFragment.onBtnCreateClick(it)
        }
    }

    private fun onBindWarningMsgViewHolder(holder: WarningMsgViewHolder) {
        if (user.showFormsWarnings) {
            holder.msg.text = warningMsg
            holder.crossBtn.visibility = View.VISIBLE
            holder.user = user;
        } else {
            holder.layout.visibility = View.GONE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = holder.itemViewType
        var form: Form? = null
        if (position >= getIndexOfBtn() + 1)
            form = user.plans[position - (getIndexOfBtn() + 1)].form
        when (viewType) {
            R.layout.warning_msg_row ->
                onBindWarningMsgViewHolder(holder as WarningMsgViewHolder)
            R.layout.active_form_row ->
                onBindActiveFormViewHolder(holder as ActiveFormViewHolder, user.activePlan?.form!!)
            R.layout.button_row -> onBindBtnViewHolder(holder as BtnViewHolder)
            R.layout.not_active_from_row ->
                onBindNotActiveFormViewHolder(holder as NotActiveViewHolder, form!!)
        }
    }

    override fun getItemCount(): Int = (getIndexOfBtn() + 1) + user.plans.size
}