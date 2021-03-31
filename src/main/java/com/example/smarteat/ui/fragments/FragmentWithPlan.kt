package com.example.smarteat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.adapters.DayAdapter
import com.example.smarteat.models.User

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWithPlan.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWithPlan(private var user: User) : Fragment() {
    private lateinit var daysRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_with_plan, container, false)
        daysRecyclerView = view.findViewById(R.id.fragment_with_plan__days)
        daysRecyclerView.layoutManager = object : LinearLayoutManager(view.context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        daysRecyclerView.adapter = DayAdapter(user.activePlan!!.weeks[0])

        return view
    }
}