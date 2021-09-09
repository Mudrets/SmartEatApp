package com.example.smarteat.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.toolbox.Volley
import com.example.smarteat.R
import com.example.smarteat.models.Form
import com.example.smarteat.ui.activities.MainActivity
import com.example.smarteat.ui.dialogs.DialogOneButton
import com.example.smarteat.ui.dialogs.DialogWithoutButton
import com.example.smarteat.utils.FieldsChecker
import com.example.smarteat.utils.JsonConverter
import com.example.smarteat.utils.RequestSender
import com.example.smarteat.utils.StyleManager
import com.google.android.material.button.MaterialButton

/**
 * A simple [Fragment] subclass.
 * Use the [CookingPreferencesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CookingPreferencesFragment(
    private val form: Form,
    private val countOfPages: Int,
    private val parentActivity: MainActivity? = null,
) : Fragment(), Slide {
    override var prevFragment: Fragment? = null
    override var nextFragment: Fragment? = null
    private lateinit var spinner: Spinner
    private lateinit var spinnerWrapper: RelativeLayout
    private lateinit var warningMsg: TextView
    private lateinit var backBtn: MaterialButton
    private lateinit var nextBtn: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cooking_preferences, container, false)

        spinner = view.findViewById(R.id.fragment_cooking_preferences__cooking_preferences_spinner)
        val adapter = object : ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_dropdown_item
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                if (position == count) {
                    (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                    (v.findViewById<View>(android.R.id.text1) as TextView).hint = getItem(count)
                }
                return v
            }

            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        val cookingInd = if (form.cookingsPerWeek != -1) form.cookingsPerWeek else adapter.count

        adapter.addAll(*resources.getStringArray(R.array.cooking_preferences))
        spinner.adapter = adapter
        spinner.setSelection(cookingInd)
        spinner.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        spinnerWrapper =
            view.findViewById(R.id.fragment_cooking_preferences__cooking_preferences_selector)
        warningMsg = view.findViewById(R.id.fragment_cooking_preferences__warning_msg)
        warningMsg.text = ""

        backBtn = view.findViewById(R.id.fragment_cooking_preferences__back_btn)
        backBtn.setOnClickListener {
            onBackButtonClick(it)
        }

        nextBtn = view.findViewById(R.id.fragment_cooking_preferences__next_btn)
        nextBtn.setOnClickListener {
            onNextButtonClick(it)
        }

        val counterTextView =
            view.findViewById<TextView>(R.id.fragment_cooking_preferences__counter)
        counterTextView.text = "${countOfPages - 1} из $countOfPages"


        return view
    }

    private fun onBackButtonClick(view: View) {
        goToPrev()
    }

    private fun onNextButtonClick(view: View) {
        StyleManager.setBackground(StyleManager.selectorNormalBackground, spinnerWrapper)
        warningMsg.text = ""
        if (FieldsChecker.checkAllSpinners(spinner).size == 0) {
            form.cookingsPerWeek = spinner.selectedItemPosition
            parentActivity?.progressBar?.visibility = ProgressBar.VISIBLE
            nextBtn.isEnabled = false
            backBtn.isEnabled = false
            val requestJson = JsonConverter.formToJsonWithoutDailyPFC(form)
            val request = RequestSender.getJsonRequestJsonResponse(
                RequestSender.GET_CPFC_URL,
                requestJson,
                { response ->
                    val resultJson = response.getJSONObject("result")
                    form.dailyCal = resultJson.getDouble("dailyKcal").toInt()
                    form.baseCal = resultJson.getDouble("baseKcal").toInt()
                    form.dailyProtein = resultJson.getDouble("proteins").toInt()
                    form.dailyCarbon = resultJson.getDouble("carbons").toInt()
                    form.dailyFat = resultJson.getDouble("fats").toInt()
                    form.kcals = resultJson.getDouble("kcals").toInt()

                    parentActivity?.progressBar?.visibility = ProgressBar.GONE
                    nextBtn.isEnabled = true
                    backBtn.isEnabled = true

                    goToNext()
                },
                { error ->
                    val dialog = DialogOneButton(
                        "Ошибка",
                        "Не удалось создать план, проверьте свое интернет соедниение и попробуйте еще раз.",
                        "К анкетам",
                        {
                            parentActivity?.makeCurrentFragment(parentActivity?.formsFragment)
                        }
                    )
                    parentActivity?.showDialog(dialog)
                    Log.e("error", error.toString())
                    parentActivity?.progressBar?.visibility = ProgressBar.GONE
                    nextBtn.isEnabled = true
                    backBtn.isEnabled = true
                    goToNext()
                }
            )
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(request)
        } else {
            StyleManager.setBackground(StyleManager.selectorWarningBackground, spinnerWrapper)
            warningMsg.text = "Выберите один из предложенных вариантов"
        }
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

    override fun goToPrev() {
        if (prevFragment != null)
            parentActivity?.makeCurrentFragment(prevFragment!!)
    }

    override fun goToNext() {
        if (nextFragment != null)
            parentActivity?.makeCurrentFragment(nextFragment!!)
    }
}