package com.example.smarteat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.adapters.DayAdapter
import com.example.smarteat.models.Day
import com.example.smarteat.models.User
import com.example.smarteat.ui.activities.MainActivity

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWithPlan.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWithPlan(
    internal var user: User,
    internal val parentActivity: MainActivity
) : Fragment() {
    private lateinit var daysRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_with_plan, container, false)

        val header: TextView = view.findViewById(R.id.fragment_with_plan__header)
        val warningMsg: String
        val btnText: String
        val onClick: (View) -> Unit
        val days: ArrayList<Day>
        if (user.activePlan == null) {
            header.text = "План питания"
            warningMsg = resources.getString(R.string.warning_msg_about_creating_plan)
            btnText = "Заполнить анкету"
            onClick = {
                parentActivity.bottomNavigation?.menu?.getItem(3)?.isChecked = true
                parentActivity.makeCurrentFragment(parentActivity.formsFragment)
            }
            days = ArrayList()
        } else if(user.activePlan != null && !user.isSubscribed && !user.isFirstWeek) {
            header.text = "План питания"
            warningMsg = "Предоставленная вам пробная неделя закончилась. Для получения доступа ко всему " +
                    "функционалу приложения оформите подписку."
            btnText = "Оформить подписку"
            days = ArrayList()
            onClick = {
                parentActivity.bottomNavigation?.menu?.getItem(4)?.isChecked = true
                parentActivity.makeCurrentFragment(parentActivity.profileFragment)
            }
        } else if (user.activePlan != null && !user.isSubscribed) {
            header.text = "Пробная неделя"
            warningMsg =
                "Это пробный план на 1 неделю, основанный на информации, которую вы указали в анкете."
            btnText = "Оформить подписку"
            onClick = {
                parentActivity.bottomNavigation?.menu?.getItem(4)?.isChecked = true
                parentActivity.makeCurrentFragment(parentActivity.profileFragment)
            }
            days = user.activePlan?.weeks?.get((user.activePlan?.numOfWeek ?: 1) - 1) ?: ArrayList()
        } else {
            header.text = "Неделя ${user.activePlan!!.numOfWeek}"
            warningMsg = ""
            btnText = ""
            onClick = {}
            val numOfWeek = user.activePlan!!.numOfWeek
            days = user.activePlan!!.weeks[numOfWeek - 1]
        }

        daysRecyclerView = view.findViewById(R.id.fragment_with_plan__days)
        daysRecyclerView.layoutManager = object : LinearLayoutManager(view.context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        daysRecyclerView.adapter = DayAdapter(
            days,
            warningMsg,
            btnText,
            onClick,
            this
        )
        return view
    }
}