package com.example.smarteat.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.adapters.CheckBoxesAdapter
import com.example.smarteat.models.Form
import com.example.smarteat.ui.activities.MainActivity
import com.example.smarteat.ui.dialogs.DialogWithoutButton
import com.example.smarteat.utils.RecipesIngredientsInfo
import com.google.android.material.button.MaterialButton

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionsFragment(
    private val products: ArrayList<String>,
    private val form: Form,
    private val numQuestion: Int,
    private val countQuestions: Int,
    internal var parentActivity: MainActivity? = null
) : Fragment(), Slide {
    override var prevFragment: Fragment? = null
    override var nextFragment: Fragment? = null
    private lateinit var productsCheckBoxes: RecyclerView
    private val showedProducts = ArrayList<String>()
    private val countProducts: Int
    private lateinit var moreBtn: TextView
    private lateinit var warningMessage: TextView
    private val countChecked: Int
        get() = products.count { form.ingredients.contains(RecipesIngredientsInfo.quizNameToName[it]) }
    private val images = arrayOf(
        R.mipmap.ic_cereal_foreground,
        R.mipmap.ic_bread_foreground,
        R.mipmap.ic_meat_foreground,
        R.mipmap.ic_seafood_foreground,
        R.mipmap.ic_milk_foreground,
        R.mipmap.ic_vegetables_foreground,
        R.mipmap.ic_fruits_foreground,
        R.mipmap.ic_eggs_foreground,
        R.mipmap.ic_oil_foreground,
        R.mipmap.ic_honey_foreground,
        R.mipmap.ic_nuts_foreground,
        R.mipmap.ic_dates_foreground,
        R.mipmap.ic_corn_foreground,
        R.mipmap.ic_spice_foreground,
        R.mipmap.ic_sauce_foreground,
        R.mipmap.ic_tofu_foreground,
        R.mipmap.ic_bakery_foreground
    )


    init {
        val pagesWithTwoProducts = arrayOf(2, 7)
        val pagesWithOneProducts = arrayOf(8, 10)
        countProducts = when {
            pagesWithTwoProducts.contains(numQuestion) -> 2
            pagesWithOneProducts.contains(numQuestion) -> 1
            else -> 0
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_questions, container, false)
        val backButton = view.findViewById<MaterialButton>(R.id.fragment_questions__back_btn)
        backButton.setOnClickListener {
            onBackButtonClick(it)
        }

        val icon: ImageView = view.findViewById(R.id.fragment_questions__icon)
        icon.setImageDrawable(resources.getDrawable(images[numQuestion - 2], null))

        warningMessage = view.findViewById(R.id.fragment_questions__warning_msg)
        updateWarningMessage()

        val counterText: TextView = view.findViewById(R.id.fragment_questions__counter)
        counterText.text = "$numQuestion из $countQuestions"

        val nextButton = view.findViewById<MaterialButton>(R.id.fragment_questions__next_btn)
        nextButton.setOnClickListener {
            onNextButtonClick(it)
        }

        productsCheckBoxes = view.findViewById(R.id.fragment_questions__checkboxes)
        productsCheckBoxes.layoutManager = object : LinearLayoutManager(view.context) {
            override fun canScrollVertically(): Boolean = false
        }
        productsCheckBoxes.adapter = CheckBoxesAdapter(showedProducts, form, this)

        moreBtn = view.findViewById(R.id.fragment_questions__more_btn)
        moreBtn.setOnClickListener {
            addProductsToShowedValues()
        }
        if (showedProducts.size == 0)
            addProductsToShowedValues()
        if (showedProducts.size == products.size)
            moreBtn.visibility = View.GONE

        val questionsHeader: TextView = view.findViewById(R.id.fragment_questions__question)
        val questions = resources.getStringArray(R.array.questions)
        questionsHeader.text = questions[numQuestion - 2]
        return view
    }

    private fun onBackButtonClick(view: View) {
        goToPrev()
    }

    private fun onNextButtonClick(view: View) {
        if (countProducts - countChecked <= 0) {
            goToNext()
        } else {
            val msg: String
            if (countProducts - countChecked == 1) {
                msg =
                    "Выберите еще хотя бы ${countProducts - countChecked} продукт"
            } else if (countProducts - countChecked in 2..4) {
                msg =
                    "Выберите еще хотя бы ${countProducts - countChecked} продукта"
            } else {
                msg =
                    "Выберите еще хотя бы ${countProducts - countChecked} продуктов"
            }
            val warningDialog = DialogWithoutButton(
                "Недостаточно продуктов",
                msg
            )
            parentActivity?.showDialog(warningDialog)
        }
    }

    private fun addProductsToShowedValues() {
        val positionStart = showedProducts.size
        val difference = products.size - showedProducts.size
        if (difference > 5) {
            for (i in showedProducts.size until showedProducts.size + 5)
                showedProducts.add(products[i])
            productsCheckBoxes.adapter?.notifyItemRangeInserted(positionStart, 5)
        } else {
            for (i in showedProducts.size until products.size)
                showedProducts.add(products[i])
            productsCheckBoxes.adapter?.notifyItemRangeInserted(positionStart, difference)
            moreBtn.visibility = View.GONE
        }
    }

    internal fun updateWarningMessage() {
        warningMessage.text = ""
        if (countProducts - countChecked > 0) {
            if (countProducts - countChecked == 1) {
                warningMessage.text =
                    "Выберите еще хотя бы ${countProducts - countChecked} продукт"
            } else if (countProducts - countChecked in 2..4) {
                warningMessage.text =
                    "Выберите еще хотя бы ${countProducts - countChecked} продукта"
            } else {
                warningMessage.text =
                    "Выберите еще хотя бы ${countProducts - countChecked} продуктов"
            }
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