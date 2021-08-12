package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.vh.PointViewHolder

class PointAdapter(
    private val points: Array<String>
) : RecyclerView.Adapter<PointViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return R.layout.point_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return PointViewHolder(view)
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.textView.text = points[position]
    }

    override fun getItemCount(): Int = points.size
}