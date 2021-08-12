package com.example.smarteat.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.smarteat.R
import com.example.smarteat.models.Form
import com.example.smarteat.ui.activities.MainActivity
import com.google.android.material.button.MaterialButton
import java.lang.NumberFormatException

class EditCpfcFragment(
    private val form: Form,
    private val parent: MainActivity
) : Fragment() {
    private lateinit var generalExchange: TextView
    private lateinit var countCalories: TextView
    private lateinit var proteinsField: EditText
    private lateinit var fatsField: EditText
    private lateinit var carbonsField: EditText
    private lateinit var caloriesField: EditText
    private lateinit var proteinsPercentField: EditText
    private lateinit var fatsPercentField: EditText
    private lateinit var carbonsPercentField: EditText
    private lateinit var resultProteins: TextView
    private lateinit var resultFats: TextView
    private lateinit var resultCarbons: TextView
    private lateinit var resultCalories: TextView
    private lateinit var resultSurplus: TextView
    private lateinit var resultSurplusHeader: TextView
    private lateinit var warningMessage: TextView
    private lateinit var saveButton: MaterialButton
    private var startCountCalories: Int = -1
    private var fatPer = -1
    private var carbonPer = -1
    private var proteinPer = -1
    private var lastValidFatPer = 0
    private var lastValidProteinPer = 0
    private var lastValidCarbonPer = 0
    private val checkPercentsFields = { it: Editable? ->
        val value: Double
        val proteinOldVal = form.dailyProtein
        val fatOldVal = form.dailyFat
        val carbonOldVal = form.dailyCarbon
        try {
            value = it.toString().replace(',', '.').toDouble()
            if (value > 0) {
                val sumOfPer = sumOfAllPercents()
                if (sumOfPer > 100) {
                    warningMessage.text = "Сумма не равна 100%, лишних ${sumOfPer - 100}%"
                    proteinPer = proteinsPercentField.text.toString().toInt()
                    fatPer = fatsPercentField.text.toString().toInt()
                    carbonPer = carbonsPercentField.text.toString().toInt()
                } else if (sumOfPer < 100) {
                    warningMessage.text =
                        "Сумма не равна 100%, не хватает ${100 - sumOfPer}%"
                    proteinPer = proteinsPercentField.text.toString().toInt()
                    fatPer = fatsPercentField.text.toString().toInt()
                    carbonPer = carbonsPercentField.text.toString().toInt()
                } else {
                    form.dailyProtein =
                        (form.dailyCal * proteinsPercentField.text.toString().toInt() / 400.0).toInt()
                    form.dailyFat =
                        (form.dailyCal * fatsPercentField.text.toString().toInt() / 900.0).toInt()
                    form.dailyCarbon =
                        (form.dailyCal * carbonsPercentField.text.toString().toInt() / 400.0).toInt()
                    proteinPer = -1
                    fatPer = -1
                    carbonPer = -1
                    warningMessage.text = ""
                }
            }
        } catch (ex: NumberFormatException) {
            form.dailyCarbon = carbonOldVal
            form.dailyFat = fatOldVal
            form.dailyProtein = proteinOldVal
        } finally {
            setValues()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_cpfc, container, false)

        initializeComponents(view)
        setValues()
        setTextChangedListenerToOneKiloFields()
        setTextChangeListenerToPercentFields()

        caloriesField.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val value: Double
                val oldVal = form.dailyCal
                try {
                    value = caloriesField.text.toString().toDouble()
                    if (value >= generalExchange.text.toString().toInt()) {
                        form.dailyCal = value.toInt()
                        form.dailyProtein =
                            (value * lastValidProteinPer / 400).toInt()
                        form.dailyFat =
                            (value * lastValidFatPer / 900).toInt()
                        form.dailyCarbon =
                            (value * lastValidCarbonPer / 400).toInt()
                    }
                } catch (ex: NumberFormatException) {
                    caloriesField.setText(oldVal.toString())
                } finally {
                    setValues()
                }
            }
        }

        saveButton.setOnClickListener {
            if (sumOfAllPercents() == 100)
                parent.showLastSlide()
        }

        return view
    }

    private fun sumOfAllPercents(): Int {
        return proteinsPercentField.text.toString().toInt() +
                    fatsPercentField.text.toString().toInt() +
                    carbonsPercentField.text.toString().toInt()
    }

    private fun initializeComponents(view: View) {
        generalExchange =
            view.findViewById(R.id.fragment_edit_cpfc__general_exchange_count_calories)
        countCalories = view.findViewById(R.id.fragment_edit_cpfc__count_calories)
        proteinsField = view.findViewById(R.id.fragment_edit_cpfc__proteins_field)
        fatsField = view.findViewById(R.id.fragment_edit_cpfc__fats_field)
        carbonsField = view.findViewById(R.id.fragment_edit_cpfc__carbohydrates_field)
        caloriesField = view.findViewById(R.id.fragment_edit_cpfc__amount_calories_of_plan_field)
        proteinsPercentField = view.findViewById(R.id.fragment_edit_cpfc__proteins_field_percent)
        fatsPercentField = view.findViewById(R.id.fragment_edit_cpfc__fats_field_percent)
        carbonsPercentField =
            view.findViewById(R.id.fragment_edit_cpfc__carbohydrates_field_percent)
        resultProteins = view.findViewById(R.id.fragment_edit_cpfc__proteins_value)
        resultFats = view.findViewById(R.id.fragment_edit_cpfc__fats_value)
        resultCarbons = view.findViewById(R.id.fragment_edit_cpfc__carbohydrates_value)
        resultCalories = view.findViewById(R.id.fragment_edit_cpfc__calories_value)
        resultSurplus = view.findViewById(R.id.fragment_edit_cpfc__surplus)
        resultSurplusHeader = view.findViewById(R.id.fragment_edit_cpfc__surplus_header)
        saveButton = view.findViewById(R.id.fragment_edit_cpfc__save_btn)
        warningMessage = view.findViewById(R.id.fragment_edit_cpfc__warning_msg)
        startCountCalories = form.dailyCal
    }

    private fun setValues() {
        countCalories.text = startCountCalories.toString()
        generalExchange.text = form.baseCal.toString()
        proteinsField.setText(String.format("%.2f", form.dailyProtein.toDouble() / form.weight))
        fatsField.setText(String.format("%.2f", form.dailyFat.toDouble() / form.weight))
        carbonsField.setText(String.format("%.2f", form.dailyCarbon.toDouble() / form.weight))
        caloriesField.setText(form.dailyCal.toString())
        if (fatPer == -1 && carbonPer == -1 && proteinPer == -1) {
            lastValidProteinPer = ((form.dailyProtein * 400).toDouble() / form.dailyCal + 0.5).toInt()
            lastValidFatPer = ((form.dailyFat * 900).toDouble() / form.dailyCal + 0.5).toInt()
            lastValidCarbonPer = 100 - (lastValidFatPer + lastValidProteinPer)
            proteinsPercentField.setText(lastValidProteinPer.toString())
            fatsPercentField.setText(lastValidFatPer.toString())
            carbonsPercentField.setText(lastValidCarbonPer.toString())
        } else {
            proteinsPercentField.setText(proteinPer.toString())
            fatsPercentField.setText(fatPer.toString())
            carbonsPercentField.setText(carbonPer.toString())
        }
        resultProteins.text = form.dailyProtein.toString()
        resultFats.text = form.dailyFat.toString()
        resultCarbons.text = form.dailyCarbon.toString()
        resultCalories.text = form.dailyCal.toString()
        if (startCountCalories < form.dailyCal) {
            resultSurplusHeader.visibility = View.VISIBLE
            resultSurplus.visibility = View.VISIBLE
            resultSurplus.text = (form.dailyCal - startCountCalories).toString()
        } else {
            resultSurplusHeader.visibility = View.GONE
            resultSurplus.visibility = View.GONE
        }
    }

    private fun setTextChangedListenerToOneKiloFields() {
        proteinsField.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val value: Double
                try {
                    value = proteinsField.text.toString().replace(',', '.').toDouble()
                    if (value > 0 && form.dailyProtein * 4 + form.dailyFat * 9 + form.dailyCarbon * 4 > generalExchange.text.toString().toInt()) {
                        form.dailyProtein = (value * form.weight).toInt()
                        form.dailyCal =
                            form.dailyProtein * 4 + form.dailyFat * 9 + form.dailyCarbon * 4
                    }
                } catch (ex: NumberFormatException) {
                    proteinsField.setText(
                        String.format(
                            "%.2f",
                            form.dailyProtein.toDouble() / form.weight
                        )
                    )
                } finally {
                    setValues()
                }
            }
        }

        fatsField.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val value: Double
                try {
                    value = fatsField.text.toString().replace(',', '.').toDouble()
                    if (value > 0 && form.dailyProtein * 4 + form.dailyFat * 9 + form.dailyCarbon * 4 > generalExchange.text.toString().toInt()) {
                        form.dailyFat = (value * form.weight).toInt()
                        form.dailyCal =
                            form.dailyProtein * 4 + form.dailyFat * 9 + form.dailyCarbon * 4
                    }
                } catch (ex: NumberFormatException) {
                    fatsField.setText(String.format("%.2f", form.dailyFat.toDouble() / form.weight))
                } finally {
                    setValues()
                }
            }
        }

        carbonsField.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val value: Double
                try {
                    value = carbonsField.text.toString().replace(',', '.').toDouble()
                    if (value > 0 && form.dailyProtein * 4 + form.dailyFat * 9 + form.dailyCarbon * 4 > generalExchange.text.toString().toInt()) {
                        form.dailyCarbon = (value * form.weight).toInt()
                        form.dailyCal =
                            form.dailyProtein * 4 + form.dailyFat * 9 + form.dailyCarbon * 4
                    }
                } catch (ex: NumberFormatException) {
                    carbonsField.setText(
                        String.format(
                            "%.2f",
                            form.dailyCarbon.toDouble() / form.weight
                        )
                    )
                } finally {
                    setValues()
                }
            }
        }
    }

    private fun setTextChangeListenerToPercentFields() {
        proteinsPercentField.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
                checkPercentsFields(proteinsPercentField.text)
        }

        fatsPercentField.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
                checkPercentsFields(fatsPercentField.text)
        }

        carbonsPercentField.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
                checkPercentsFields(carbonsPercentField.text)
        }

    }
}