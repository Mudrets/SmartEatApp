package com.example.smarteat.ui.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.adapters.CheckBoxesAdapter
import com.example.smarteat.adapters.SnackCheckBoxesAdapter
import com.example.smarteat.models.Form
import com.example.smarteat.ui.activities.MainActivity
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.collections.ArrayList

class CountOfSnacks(
    private val form: Form,
    private val parentActivity: MainActivity? = null,
    private val countOfPages: Int,
    private val numQuestion: Int,
) : Fragment(), Slide {
    override var prevFragment: Fragment? = null
    override var nextFragment: Fragment? = null
    private lateinit var checkBoxRecyclerView: RecyclerView
    private lateinit var warningMsg: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_count_of_snacks, container, false)

        warningMsg = view.findViewById(R.id.fragment_count_of_snacks__warning_msg)
        warningMsg.text = ""

        val counterText: TextView = view.findViewById(R.id.fragment_count_of_snacks__counter)
        counterText.text = "$numQuestion из $countOfPages"

        val nextButton = view.findViewById<MaterialButton>(R.id.fragment_count_of_snacks__next_btn)
        nextButton.setOnClickListener {
            goToNext()
        }

        val backBtn = view.findViewById<MaterialButton>(R.id.fragment_count_of_snacks__back_btn)
        backBtn.setOnClickListener {
            goToPrev()
        }

        checkBoxRecyclerView = view.findViewById(R.id.fragment_count_of_snacks__checkboxes)
        checkBoxRecyclerView.layoutManager = object : LinearLayoutManager(view.context) {
            override fun canScrollVertically(): Boolean = false
        }
        val snacks = ArrayList<String>(Arrays.asList(
            "Утром",
            "Днем",
            "Вечером"
        ))
        checkBoxRecyclerView.adapter = SnackCheckBoxesAdapter(snacks, form)


        return view
    }

    override fun goToPrev() {
        if (prevFragment != null)
            parentActivity?.makeCurrentFragment(prevFragment!!)
    }

    override fun goToNext() {
        if (nextFragment != null)
            parentActivity?.makeCurrentFragment(nextFragment!!)
    }

    override fun setNext(fragment: Fragment?) {
        if (fragment != null && fragment is Slide)
            fragment.prevFragment = this
        nextFragment = fragment
    }

    override fun setPrev(fragment: Fragment?) {
        if (fragment != null && fragment is Slide)
            fragment.nextFragment = this
        prevFragment = fragment
    }
}