package com.example.smarteat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.smarteat.R
import com.example.smarteat.models.Form
import com.example.smarteat.ui.activities.MainActivity
import com.google.android.material.button.MaterialButton
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 * Use the [FinalSettingsOfFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinalSettingsOfFormFragment(
    private val form: Form,
    private val countOfPages: Int,
    internal var parentActivity: MainActivity? = null,
    private var editCpfcFragment: EditCpfcFragment
) : Fragment(), Slide {
    override var prevFragment: Fragment? = null
    override var nextFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_final_settings_of_form, container, false)

        val counterTextView = view.findViewById<TextView>(R.id.fragment_final_settings_of_form__counter)
        counterTextView.text = "$countOfPages из $countOfPages"

        val lifeStyles = resources.getStringArray(R.array.activity_level)
        val goals = resources.getStringArray(R.array.goals)

        //TODO: create a new and show more settings for this plan
        val detailEditButton: MaterialButton = view.findViewById(R.id.fragment_final_settings_of_form__edit_cpfc_btn)
        val heightField: TextView = view.findViewById(R.id.fragment_final_settings_of_form__height_value)
        val goalField: TextView = view.findViewById(R.id.fragment_final_settings_of_form__goal_value)
        val weightField: TextView = view.findViewById(R.id.fragment_final_settings_of_form__weight_value)
        val ageField: TextView = view.findViewById(R.id.fragment_final_settings_of_form__age_value)
        val genderField: TextView = view.findViewById(R.id.fragment_final_settings_of_form__sex_value)
        val lifeStyleField: TextView = view.findViewById(R.id.fragment_final_settings_of_form__activity_level_value)
        val proteinField: TextView = view.findViewById(R.id.fragment_final_settings_of_form__protein_value)
        val fatsField: TextView = view.findViewById(R.id.fragment_final_settings_of_form__fats_value)
        val carbonField: TextView = view.findViewById(R.id.fragment_final_settings_of_form__carbohydrates_value)
        val kcalField: TextView = view.findViewById(R.id.fragment_final_settings_of_form__calories_value)

        heightField.text = form.height.toString()
        ageField.text = form.age.toString()
        weightField.text = form.weight.toString()
        genderField.text = if (form.gender == 0) "Ж" else "М"
        proteinField.text = form.dailyProtein.toString()
        fatsField.text = form.dailyFat.toString()
        carbonField.text = form.dailyCarbon.toString()
        kcalField.text = form.dailyCal.toString()

        lifeStyleField.text = lifeStyles[form.lifeStyle]
        goalField.text = goals[form.goal]

        val backBtn = view.findViewById<MaterialButton>(R.id.fragment_final_settings_of_form__back_btn)
        backBtn.setOnClickListener {
            goToPrev()
        }

        val saveBtn = view.findViewById<MaterialButton>(R.id.fragment_final_settings_of_form__save_btn)
        saveBtn.setOnClickListener {
            parentActivity?.createPlanByForm(form)
        }

        detailEditButton.setOnClickListener {
            parentActivity?.makeCurrentFragment(editCpfcFragment)
        }

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