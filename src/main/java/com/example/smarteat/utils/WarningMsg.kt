package com.example.smarteat.utils

import com.example.smarteat.R

object WarningMsg {
    private const val PERSONAL_FORM_HEIGHT_MSG = "Рост должен быть в диапазоне 120-250 см"
    private const val PERSONAL_FORM_WEIGHT_MSG = "Вес в диапазоне 30-250 кг"
    private const val PERSONAL_FORM_AGE_MSG = "Вам должено быть не менее 18 лет"
    private const val PERSONAL_FORM_GOAL_SPINNER_MSG = "Укажите вашу цель"
    private const val PERSONAL_FORM_COUNT_WORKOUTS_SPINNER_MSG = "Как часто вы тренируетесь"
    private const val PERSONAL_FORM_FITNESS_LEVEL_SPINNER_MSG = "Укажите физическую подготовку"
    private const val PERSONAL_FORM_ACTIVITY_LEVEL_SPINNER_MSG = "Укажите уровень физической активности"
    private const val PERSONAL_FORM_MOTHERHOOD_SPINNER_MSG = "Укажите являетесь ли вы беременной/кормящей"

    fun getErrorMsgForField(id: Int): String =
        when (id) {
            R.id.fragment_personal_form__height_field -> PERSONAL_FORM_HEIGHT_MSG
            R.id.fragment_personal_form__weight_field -> PERSONAL_FORM_WEIGHT_MSG
            R.id.fragment_personal_form__age_field -> PERSONAL_FORM_AGE_MSG
            R.id.fragment_personal_form__goal_selector -> PERSONAL_FORM_GOAL_SPINNER_MSG
            R.id.fragment_personal_form__count_of_workouts_selector -> PERSONAL_FORM_COUNT_WORKOUTS_SPINNER_MSG
            R.id.fragment_personal_form__fitness_level_selector -> PERSONAL_FORM_FITNESS_LEVEL_SPINNER_MSG
            R.id.fragment_personal_form__activity_level_selector -> PERSONAL_FORM_ACTIVITY_LEVEL_SPINNER_MSG
            R.id.fragment_personal_form__motherhood_selector -> PERSONAL_FORM_MOTHERHOOD_SPINNER_MSG
            else -> ""
        }
}