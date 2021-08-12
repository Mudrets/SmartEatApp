package com.example.smarteat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.adapters.AchievementAdapter
import com.example.smarteat.models.User
import com.example.smarteat.ui.activities.AboutUsActivity
import com.example.smarteat.ui.activities.EditProfileActivity
import com.example.smarteat.ui.activities.MainActivity
import com.example.smarteat.ui.dialogs.DialogOneButton
import com.example.smarteat.ui.dialogs.DialogWithRecycler
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import java.text.DateFormat
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment(
    private val user: User,
    private val parentActivity: MainActivity
) : Fragment() {

    private lateinit var achievementAdapter: RecyclerView
    internal lateinit var username: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        username = view.findViewById(R.id.fragment_profile__username)
        username.text = user.fullName

        val endDate: TextView = view.findViewById(R.id.fragment_profile__end_date_subscribe)
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val subscribeBtn: MaterialButton = view.findViewById(R.id.fragment_profile__subscribe_btn)
        val points = resources.getStringArray(R.array.subscribePoints)
        subscribeBtn.setOnClickListener {
            val dialog = DialogWithRecycler(
                "Что дает подписка?",
                points,
                "Оформить подписку"
            ) {
                user.isSubscribed = true
                subscribeBtn.visibility = View.GONE
                endDate.text = "Подписка действует до ${dateFormatter.format(user.subscribeEndDate)}"
                endDate.visibility = View.VISIBLE
            }
            parentActivity.showDialog(dialog)
        }
        if (user.isSubscribed) {
            subscribeBtn.visibility = View.GONE
            endDate.text = "Подписка действует до ${dateFormatter.format(user.subscribeEndDate)}"
            endDate.visibility = View.VISIBLE
        } else {
            subscribeBtn.visibility = View.VISIBLE
            endDate.visibility = View.GONE
        }

        val drawer: DrawerLayout = view.findViewById(R.id.profile_fragment__drawer)
        val menuToggle: ImageView = view.findViewById(R.id.fragment_profile__menu_toggle)
        menuToggle.setOnClickListener {
            drawer.openDrawer(GravityCompat.END)
        }

        val editBtn: MaterialButton = view.findViewById(R.id.fragment_profile__menu_edit_btn)
        val aboutUs: MaterialButton = view.findViewById(R.id.fragment_profile__menu_about_us_btn)
        val exitBtn: MaterialButton = view.findViewById(R.id.fragment_profile__menu_exit)

        editBtn.setOnClickListener {
            parentActivity.showActivity(EditProfileActivity(parentActivity).javaClass)
            username.text = user.fullName
        }
        aboutUs.setOnClickListener {
            parentActivity.showActivity(AboutUsActivity().javaClass)
        }
        exitBtn.setOnClickListener {
            parentActivity.logOut()
        }

        achievementAdapter = view.findViewById(R.id.activity_profile__achievements)
        achievementAdapter.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        achievementAdapter.adapter = AchievementAdapter(user.achievements)

        return view
    }
}