package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Day
import com.example.smarteat.models.Form
import com.example.smarteat.ui.fragments.FragmentWithPlan
import com.example.smarteat.vh.BtnViewHolder
import com.example.smarteat.vh.WarningMsgViewHolder

class DayAdapter(
    private val days: ArrayList<Day>,
    private val warningMsg: String = "",
    private val textForBtn: String = "",
    private val onClickListener: (view: View) -> Unit? = {},
    private val parentFragment: FragmentWithPlan
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val caloriesTextView: TextView = itemView.findViewById(R.id.day_row__num_calories)
        val dayTextView: TextView = itemView.findViewById(R.id.day_row__num_of_day)
        val eatingRecyclerView: RecyclerView = itemView.findViewById(R.id.day_row__eating_recycler)
        val arrowIcon: ImageView = itemView.findViewById(R.id.day_row__main_arrow)
        val headerWrapper: ViewGroup = itemView.findViewById(R.id.day_row__header_wrapper)
        val view: View = itemView

        init {
            val clickListener = View.OnClickListener { _ ->
                val day = days[adapterPosition - 2]
                if (day.isExpanded)
                    for (eating in day.eating)
                        eating.isExpanded = true
                day.isExpanded = !day.isExpanded
                notifyItemChanged(adapterPosition)
            }
            headerWrapper.setOnClickListener(clickListener)
            dayTextView.setOnClickListener(clickListener)
            caloriesTextView.setOnClickListener(clickListener)
            arrowIcon.setOnClickListener(clickListener)
            eatingRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            eatingRecyclerView.isNestedScrollingEnabled = false
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.warning_msg_row
            1 -> R.layout.button_row
            else -> R.layout.day_row
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.warning_msg_row -> WarningMsgViewHolder(view)
            R.layout.button_row -> BtnViewHolder(view)
            else -> DayViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.warning_msg_row ->
                onBindWarningMsgViewHolder(holder as WarningMsgViewHolder)
            R.layout.button_row -> onBindButtonViewHolder(holder as BtnViewHolder)
            R.layout.day_row -> onBindDayViewHolder(holder as DayViewHolder, position)
        }
    }

    private fun onBindDayViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position - 2]

        val caloriesString = "${day.calories} ККАЛ"
        holder.caloriesTextView.text = caloriesString
        val numOfDay = position - 1
        val dayText = "ДЕНЬ $numOfDay"
        holder.dayTextView.text = dayText

        holder.eatingRecyclerView.adapter = EatingAdapter(day.eating, parentFragment)

        val isExpanded = day.isExpanded
        holder.arrowIcon.rotationX = if (isExpanded) 180F else 0F
        holder.eatingRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
    }

    private fun onBindWarningMsgViewHolder(holder: WarningMsgViewHolder) {
        holder.msg.text = warningMsg
        if (warningMsg == "")
            holder.layout.visibility = View.GONE
    }

    private fun onBindButtonViewHolder(holder: BtnViewHolder) {
        if (textForBtn != "") {
            holder.btn.text = textForBtn
            holder.btn.setOnClickListener {
                onClickListener(it)
            }
        } else {
            holder.btn.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = days.size + 2
}