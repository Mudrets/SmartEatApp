package com.example.smarteat.ui.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.smarteat.R
import com.example.smarteat.models.Form
import com.example.smarteat.ui.activities.MainActivity
import com.example.smarteat.utils.FieldsChecker
import com.example.smarteat.utils.StyleManager
import com.example.smarteat.utils.WarningMsg
import com.google.android.material.button.MaterialButton


/**
 * A simple [Fragment] subclass.
 * Use the [PersonalFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalFormFragment(
    private val form: Form,
    private val countFragments: Int,
    internal var parentActivity: MainActivity? = null
) : Fragment(), Slide {
    override var prevFragment: Fragment? = null
    override var nextFragment: Fragment? = null

    private lateinit var heightField: EditText
    private lateinit var weightField: EditText
    private lateinit var ageField: EditText
    private lateinit var goalSpinner: Spinner
    private lateinit var countOfWorkoutsSpinner: Spinner
    private lateinit var activityLevelSpinner: Spinner
    private lateinit var fitnessLevelSpinner: Spinner
    private lateinit var motherhoodSpinner: Spinner
    private lateinit var goalWrapper: RelativeLayout
    private lateinit var countOfWorkoutsWrapper: RelativeLayout
    private lateinit var activityLevelWrapper: RelativeLayout
    private lateinit var fitnessLevelWrapper: RelativeLayout
    private lateinit var motherhoodWrapper: RelativeLayout
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var warningTextView: TextView
    private lateinit var counterTextView: TextView
    private lateinit var motherhoodHeader: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_personal_form, container, false)

        val nextButton = view.findViewById<MaterialButton>(R.id.fragment_personal_form__next_btn)
        nextButton.setOnClickListener {
            onClickNextButton(it)
        }

        val onClickField = { _view: View ->
            _view.background = StyleManager.fieldNormalBackground
        }

        val heightContent = if (form.height != -1) form.height.toString() else ""
        val weightContent = if (form.weight != -1) form.weight.toString() else ""
        val ageContent = if (form.age != -1) form.age.toString() else ""

        heightField = view.findViewById(R.id.fragment_personal_form__height_field)
        heightField.setOnClickListener(onClickField)
        heightField.setText(heightContent)

        weightField = view.findViewById(R.id.fragment_personal_form__weight_field)
        weightField.setOnClickListener(onClickField)
        weightField.setText(weightContent)

        ageField = view.findViewById(R.id.fragment_personal_form__age_field)
        ageField.setOnClickListener(onClickField)
        ageField.setText(ageContent)

        maleRadioButton = view.findViewById(R.id.fragment_personal_form__male_radio_btn)
        maleRadioButton.setOnClickListener {
            motherhoodWrapper.visibility = View.GONE
            motherhoodHeader.visibility = View.GONE
        }
        femaleRadioButton = view.findViewById(R.id.fragment_personal_form__female_radio_btn)
        femaleRadioButton.setOnClickListener {
            motherhoodWrapper.visibility = View.VISIBLE
            motherhoodHeader.visibility = View.VISIBLE
        }

        goalWrapper = view.findViewById(R.id.fragment_personal_form__goal_selector)
        countOfWorkoutsWrapper =
            view.findViewById(R.id.fragment_personal_form__count_of_workouts_selector)
        activityLevelWrapper =
            view.findViewById(R.id.fragment_personal_form__activity_level_selector)
        fitnessLevelWrapper = view.findViewById(R.id.fragment_personal_form__fitness_level_selector)
        motherhoodWrapper = view.findViewById(R.id.fragment_personal_form__motherhood_selector)
        motherhoodHeader = view.findViewById(R.id.fragment_personal_form__motherhood_header)
        motherhoodWrapper.visibility = View.GONE
        motherhoodHeader.visibility = View.GONE
        if (form.gender == 1)
            maleRadioButton.isChecked = true
        else if (form.gender == 0) {
            femaleRadioButton.isChecked = true
            motherhoodWrapper.visibility = View.VISIBLE
            motherhoodHeader.visibility = View.VISIBLE
        }

        val elems = arrayOf(
            resources.getStringArray(R.array.goals),
            resources.getStringArray(R.array.amount_of_workouts),
            resources.getStringArray(R.array.activity_level),
            resources.getStringArray(R.array.fitness_level),
            resources.getStringArray(R.array.motherhood)
        )

        var adapters = ArrayList<ArrayAdapter<String>>()
        for (i in 0 until 5) {
            adapters.add(object : ArrayAdapter<String>(
                context!!,
                R.layout.multiline_spinner_item
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    if (position == count) {
                        (v.findViewById<View>(R.id.text) as TextView).text = ""
                        (v.findViewById<View>(R.id.text) as TextView).hint = getItem(count)
                    }
                    return v
                }

                override fun getCount(): Int {
                    return super.getCount() - 1
                }
            })
            adapters[i].setDropDownViewResource(R.layout.multiline_spinner_dropdown_item)
            adapters[i].addAll(*elems[i])
        }

        val goalInd = if (form.goal != -1) form.goal else adapters[0].count
        val countOfWorkoutsInd =
            if (form.workoutsPerWeek != -1) form.workoutsPerWeek else adapters[1].count
        val activityLevelInd = if (form.lifeStyle != -1) form.lifeStyle else adapters[2].count
        val fitnessLevelInd = if (form.fitnessLevel != -1) form.fitnessLevel else adapters[3].count
        val motherhoodInd = if (form.motherhood != -1) form.motherhood else adapters[4].count

        //TODO: find normal way to hide a drop arrow in spinner
        goalSpinner = view.findViewById(R.id.fragment_personal_form__goal_spinner)
        goalSpinner.adapter = adapters[0]
        goalSpinner.setSelection(goalInd)
        goalSpinner.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        //TODO: find normal way to hide a drop arrow in spinner
        countOfWorkoutsSpinner =
            view.findViewById(R.id.fragment_personal_form__count_of_workouts_spinner)
        countOfWorkoutsSpinner.adapter = adapters[1]
        countOfWorkoutsSpinner.setSelection(countOfWorkoutsInd)
        countOfWorkoutsSpinner.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        //TODO: find normal way to hide a drop arrow in spinner
        activityLevelSpinner =
            view.findViewById(R.id.fragment_personal_form__activity_level_spinner)
        activityLevelSpinner.adapter = adapters[2]
        activityLevelSpinner.setSelection(activityLevelInd)
        activityLevelSpinner.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        //TODO: find normal way to hide a drop arrow in spinner
        fitnessLevelSpinner = view.findViewById(R.id.fragment_personal_form__fitness_level_spinner)
        fitnessLevelSpinner.adapter = adapters[3]
        fitnessLevelSpinner.setSelection(fitnessLevelInd)
        fitnessLevelSpinner.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        motherhoodSpinner = view.findViewById(R.id.fragment_personal_form__motherhood_spinner)
        motherhoodSpinner.adapter = adapters[4]
        motherhoodSpinner.setSelection(motherhoodInd)
        motherhoodSpinner.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        warningTextView = view.findViewById(R.id.fragment_personal_form__error_msg)
        warningTextView.text = ""
        counterTextView = view.findViewById(R.id.fragment_personal_form__counter)
        counterTextView.text = "1 из $countFragments"

        return view
    }

    private fun getWarningFields(): ArrayList<EditText> {
        val fields = arrayOf(heightField, weightField, ageField)
        StyleManager.setBackground(StyleManager.fieldNormalBackground, *fields)
        val min = arrayOf(120, 30, 18)
        val max = arrayOf(250, 250, 120)
        return FieldsChecker.checkAllFields(
            { textField, val1, val2 -> FieldsChecker.isFieldHasNum(textField, val1, val2) },
            min,
            max,
            *fields
        )
    }

    private fun getWarningSpinnerWrappers(): ArrayList<RelativeLayout> {
        val spinners: Array<Spinner>
        val wrappers: Array<RelativeLayout>
        if (femaleRadioButton.isChecked) {
            spinners = arrayOf(
                goalSpinner,
                countOfWorkoutsSpinner,
                activityLevelSpinner,
                fitnessLevelSpinner,
                motherhoodSpinner
            )
            wrappers = arrayOf(
                goalWrapper,
                countOfWorkoutsWrapper,
                activityLevelWrapper,
                fitnessLevelWrapper,
                motherhoodWrapper
            )
        } else {
            spinners = arrayOf(
                goalSpinner,
                countOfWorkoutsSpinner,
                activityLevelSpinner,
                fitnessLevelSpinner
            )
            wrappers = arrayOf(
                goalWrapper,
                countOfWorkoutsWrapper,
                activityLevelWrapper,
                fitnessLevelWrapper
            )
        }

        StyleManager.setBackground(StyleManager.selectorNormalBackground, *wrappers)
        val errorSpinners = FieldsChecker.checkAllSpinners(*spinners)
        val res = ArrayList<RelativeLayout>()
        for ((index, spinner) in spinners.withIndex()) {
            if (errorSpinners.contains(spinner))
                res.add(wrappers[index])
        }
        return res
    }

    private fun onClickNextButton(view: View) {
        val errorTextFields = getWarningFields()
        val errorSpinnerWrappers = getWarningSpinnerWrappers()
        if ((errorTextFields.size == 0 && errorSpinnerWrappers.size == 0) &&
            (femaleRadioButton.isChecked || maleRadioButton.isChecked)
        ) {
            form.height = heightField.text.toString().toInt()
            form.weight = weightField.text.toString().toInt()
            form.age = ageField.text.toString().toInt()
            form.goal = goalSpinner.selectedItemPosition
            form.workoutsPerWeek = countOfWorkoutsSpinner.selectedItemPosition
            form.lifeStyle = activityLevelSpinner.selectedItemPosition
            form.fitnessLevel = fitnessLevelSpinner.selectedItemPosition
            form.gender = when {
                femaleRadioButton.isChecked -> 0
                maleRadioButton.isChecked -> 1
                else -> -1
            }
            if (femaleRadioButton.isChecked)
                form.motherhood = motherhoodSpinner.selectedItemPosition
            else
                form.motherhood = -1
            goToNext()
        } else {
            StyleManager.setBackground(
                StyleManager.fieldWarningBackground,
                *errorTextFields.toTypedArray()
            )
            StyleManager.setBackground(
                StyleManager.selectorWarningBackground,
                *errorSpinnerWrappers.toTypedArray()
            )
            var msg = ""
            if (errorTextFields.size > 0)
                msg = WarningMsg.getErrorMsgForField(errorTextFields[0].id)
            else if (errorSpinnerWrappers.size > 0)
                msg = WarningMsg.getErrorMsgForField(errorSpinnerWrappers[0].id)
            else if (!femaleRadioButton.isChecked && !maleRadioButton.isChecked)
                msg = "Выберите ваш пол"
            warningTextView.text = msg
        }
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