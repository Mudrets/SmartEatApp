package com.example.smarteat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R

class Adpter: RecyclerView.Adapter<Adpter.AdapterViewHolder>() {

    class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView
        init {
            textView = itemView.findViewById(R.id.randomText)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.test
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.textView.text = position.toString()
    }

    override fun getItemCount(): Int {
        return 100
    }
}