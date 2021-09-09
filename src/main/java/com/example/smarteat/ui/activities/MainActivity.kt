package com.example.smarteat.ui.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.example.smarteat.R
import com.example.smarteat.adapters.FormsAdapter
import com.example.smarteat.models.Achievement
import com.example.smarteat.models.Form
import com.example.smarteat.models.Plan
import com.example.smarteat.models.User
import com.example.smarteat.ui.dialogs.*
import com.example.smarteat.ui.fragments.*
import com.example.smarteat.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.IllegalStateException
import java.time.LocalDate
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), BottomFormDialog.FormDialogListener,
    AchievementChecker.AchievementListener {
    internal var bottomNavigation: BottomNavigationView? = null
    private lateinit var user: User
    internal lateinit var formsFragment: FormsFragment
    private lateinit var productsFragment: ProductsFragment
    internal lateinit var profileFragment: ProfileFragment
    private lateinit var recipesFragment: RecipesFragment
    private lateinit var fragmentWithPlan: FragmentWithPlan
    private lateinit var editCpfcFragment: EditCpfcFragment
    private lateinit var userDataFile: File
    internal lateinit var progressBar: ProgressBar
    private val slidesList: ArrayList<Slide> = ArrayList()
    private var questionsArray: ArrayList<String> = ArrayList()
    private val productArraysNames = ArrayList<Int>(
        arrayListOf(
            R.array.cereals,
            R.array.flour_products,
            R.array.meat_and_poultry,
            R.array.seafood,
            R.array.dairy_products,
            R.array.vegetables,
            R.array.fruits,
            R.array.eggs,
            R.array.oil,
            R.array.sweets,
            R.array.nuts,
            R.array.dried_fruits,
            R.array.legumes,
            R.array.toppings_and_spices,
            R.array.sauce,
            R.array.soy_products,
            R.array.impossible_to_refuse
        )
    )
    private lateinit var achievementImages: Array<Drawable?>
    private val achievementConditions = arrayOf(
        { user.firstUserEnterDay == LocalDate.now() }, //Первый вход
        { user.activePlan != null }, //Первый план пользователя
        { user.boughtAllProducts }, //Покупка всех продуктов
        { user.activePlan?.numOfWeek ?: 0 >= 2 } //Первая неделя
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        userDataFile = File(dir, "userData.json")
        val recipesDataFile = File(dir, "recipesData.json")
        val ingredientsDataFile = File(dir, "ingredientsData.json")
        val userDataStr = ReadWriter.readDataFromFile(userDataFile)
        val recipesInfoStr = ReadWriter.readDataFromFile(recipesDataFile)
        val ingredientsInfoStr = ReadWriter.readDataFromFile(ingredientsDataFile)

//        userDataFile.delete()
        achievementImages = arrayOf(
            ResourcesCompat.getDrawable(resources, R.mipmap.ic_door_foreground, null), //Первый вход
            ResourcesCompat.getDrawable(
                resources,
                R.mipmap.ic_food_tray_foreground,
                null
            ), //Первый план
            ResourcesCompat.getDrawable(
                resources,
                R.mipmap.ic_basket_foreground,
                null
            ), //Покупка всех продуктов
            ResourcesCompat.getDrawable(
                resources,
                R.mipmap.ic_date_foreground,
                null
            ) //Первая неделя
        )

        for (i in 0..3) {
            val name = resources.getStringArray(R.array.achievementNames)[i]
            val description = resources.getStringArray(R.array.achievementDescription)[i]
            val achievement = Achievement(
                name,
                description,
                achievementImages[i]!!,
                achievementConditions[i]
            )
            if (!AchievementChecker.achievements.contains(achievement))
                AchievementChecker.achievements.add(achievement)
        }

        if (userDataStr != "" && recipesInfoStr != "" && ingredientsInfoStr != "") {
            RecipesIngredientsInfo.getIngredientsInfoFromJSON(JSONArray(ingredientsInfoStr))
            RecipesIngredientsInfo.getRecipesNamesAndCookInfoIds(JSONArray(recipesInfoStr))
            val userJson = JSONObject(userDataStr)
            if (userJson.has("withExtra") && !userJson.getBoolean("withExtra")) {
                userJson.put("withExtra", true)
                val uuid = userJson
                    .getJSONArray("users")
                    .getJSONObject(0)
                    .getString("userId")
                val extraFile = File(dir, "$uuid.json")
                if (extraFile.exists()) {
                    val extraJson = JSONObject(ReadWriter.readDataFromFile(extraFile))
                    userJson.put("achievements", extraJson.getJSONArray("achievements"))
                    userJson.getJSONArray("users").getJSONObject(0)
                        .put("phone", extraJson.getString("phone"))
                    userJson.getJSONArray("users").getJSONObject(0)
                        .put("fullName", extraJson.getString("fullName"))
                    if (extraJson.has("activePlan")) {
                        userJson.put("activePlan", extraJson.getJSONObject("activePlan"))
                        if (extraJson.has("productsForWeek"))
                            userJson.getJSONArray("users")
                                .getJSONObject(0)
                                .put("productsForWeek", extraJson.getJSONArray("productsForWeek"))
                    }
                    if (extraJson.has("subscribeDate"))
                        userJson.getJSONArray("users")
                            .getJSONObject(0)
                            .put("subscribeDate", extraJson.getString("subscribeDate"))
                    if (extraJson.has("firstUserEnterDay"))
                        userJson.getJSONArray("users")
                            .getJSONObject(0)
                            .put("firstUserEnterDay", extraJson.getString("firstUserEnterDay"))
                    userJson.getJSONArray("users")
                        .getJSONObject(0)
                        .put("showFormWarnings", extraJson.getBoolean("showFormWarnings"))
                }
                extraFile.delete()
            }
            user = User(userJson)

            initializeAchievement()

            if (!user.isVisit) {
//                showWelcomeDialog()
                user.isVisit = true
                CoroutineScope(Dispatchers.IO).launch {
                    DataUpdater.updateOtherInfo(userDataFile, user)
                }
                AchievementChecker.checkAchievements()
            } else
                checkWarningAboutPlan()

            initializationComponents()
            formsFragment.parentActivity = this
            makeCurrentFragment(fragmentWithPlan)
            bottomNavigation?.menu?.getItem(2)?.isChecked = true

            bottomNavigation?.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.products -> makeCurrentFragment(productsFragment)
                    R.id.prof -> makeCurrentFragment(profileFragment)
                    R.id.recipes -> makeCurrentFragment(recipesFragment)
                    R.id.forms -> makeCurrentFragment(formsFragment)
                    R.id.plan -> makeCurrentFragment(fragmentWithPlan)
                }
                true
            }
        } else
            finish()
    }

    private fun showWarningAboutEndOfPlan() {
        val dialog = DialogOneButton(
            "Месяц подходит к концу",
            "Месяц подходит к концу, может быть стоит составить себе новый план по новой анкете?",
            "Составить новый план",
            {
                bottomNavigation?.menu?.getItem(3)?.isChecked = true
                makeCurrentFragment(formsFragment)
            },
            { AchievementChecker.checkAchievements() }
        )
        showDialog(dialog)
    }

    private fun showWarningAboutEndOfTrialPlan() {
        val dialog = DialogOneButton(
            "Пробная неделя подходит к концу",
            "Пробная неделя подходит к концу, не хотите ли вы оплатить подписку на месяц?",
            "Оформить подписку",
            {
                bottomNavigation?.menu?.getItem(4)?.isChecked = true
                makeCurrentFragment(profileFragment)
            },
            { AchievementChecker.checkAchievements() }
        )
        showDialog(dialog)
    }

    private fun showWarningAboutEndOfWeek() {
        val dialog = DialogOneButton(
            "Неделя подходит к концу",
            "Вы можете выбрать или заполнить новую анкету для следующей недели",
            "К анкетам",
            {
                bottomNavigation?.menu?.getItem(3)?.isChecked = true
                makeCurrentFragment(formsFragment)
            },
            { AchievementChecker.checkAchievements() }
        )
        showDialog(dialog)
    }

    private fun showWarningAboutEndOfTrialWeek() {
        val dialog = DialogOneButton(
            "Окончание пробной недели",
            "Пожалуйста оформите подписку, чтобы составить себе новый план питания",
            "Оформить подписку",
            {
                bottomNavigation?.menu?.getItem(4)?.isChecked = true
                makeCurrentFragment(profileFragment)
            },
            { AchievementChecker.checkAchievements() }
        )
        showDialog(dialog)
    }

    private fun checkWarningAboutPlan() {
        if (user.isSubscribed && user.activePlan != null && user.activePlan!!.currDay >= 26)
            showWarningAboutEndOfPlan()
        else if (user.isSubscribed && user.activePlan != null && (user.activePlan!!.currDay - 1) % 7 >= 5)
            showWarningAboutEndOfWeek()
        else if (!user.isSubscribed && !user.isFirstWeek)
            showWarningAboutEndOfTrialWeek()
        else if (!user.isSubscribed && user.activePlan != null && user.activePlan!!.currDay > 5)
            showWarningAboutEndOfTrialPlan()
        else
            AchievementChecker.checkAchievements()
    }

    private fun showWelcomeDialog() {
        val welcomeDialog = DialogOneButton(
            resources.getString(R.string.welcome_to_smart_eat),
            resources.getString(R.string.welcome_content),
            "Продолжить",
            { _ -> AchievementChecker.checkAchievements() },
            { AchievementChecker.checkAchievements() },
        )
        showDialog(welcomeDialog)
    }

    private fun initializeAchievement() {
        AchievementChecker.user = user
        AchievementChecker.userDataFile = userDataFile
        AchievementChecker.listeners.add(user)
        AchievementChecker.listeners.add(this)
    }

    private fun initializationComponents() {
        questionsArray = ArrayList(listOf(*resources.getStringArray(R.array.questions)))
        bottomNavigation = findViewById(R.id.bottom_navigation)
        formsFragment = FormsFragment(user)
        productsFragment = ProductsFragment(user, userDataFile, productArraysNames)
        profileFragment = ProfileFragment(user, this)
        recipesFragment = RecipesFragment(user)
        fragmentWithPlan = FragmentWithPlan(user, this)
        progressBar = findViewById(R.id.activity_main__progress_bar)
    }

    internal fun showActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    internal fun showRecipe(recipes: ArrayList<Pair<Int, Int>>, recipeName: String) {
        val newRecipes =
            recipes.filter { it.second != 1 || recipeName.toLowerCase().contains("гарнир") }
        val newRecipesFragment = RecipesFragment(user)
        newRecipesFragment.selectedRecipes = ArrayList(newRecipes)
        bottomNavigation?.menu?.getItem(1)?.isChecked = true
        makeCurrentFragment(newRecipesFragment)
    }

    internal fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commitAllowingStateLoss()
        }

    internal fun showBottomSheetDialog(bottomSheetDialog: BottomSheetDialogFragment) {
        bottomSheetDialog.show(supportFragmentManager, "formMenuDialog")
    }

    internal fun showDialog(dialog: DialogFragment) {
        try {
            dialog.show(supportFragmentManager, "one button dialog")
        } catch (ignored: IllegalStateException) {
        }
    }

    internal fun showLastSlide() {
        makeCurrentFragment(slidesList.last() as Fragment)
    }

    internal fun updateForm(form: Form, isNewForm: Boolean = false) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isNewForm)
                DataUpdater.addFormWithoutPlanToData(userDataFile, form)
        }

        slidesList.clear()
        editCpfcFragment = EditCpfcFragment(form, this)
        val personalFragment = PersonalFormFragment(form, productArraysNames.size + 3)
        personalFragment.parentActivity = this
        slidesList.add(personalFragment)

        for (i in 0 until productArraysNames.size) {
            val products = arrayListOf<String>(*resources.getStringArray(productArraysNames[i]))
            val currFragment = QuestionsFragment(
                products,
                form,
                i + 2,
                productArraysNames.size + 3,
                this
            )
            if (slidesList.size == 0)
                currFragment.setPrev(personalFragment)
            else
                currFragment.setPrev(slidesList.last() as Fragment)
            slidesList.add(currFragment)
        }

//        val countOfSnacks = CountOfSnacks(
//            form,
//            this,
//            productArraysNames.count() + 3,
//            productArraysNames.count() + 2
//        )
//        countOfSnacks.setPrev(slidesList.last() as Fragment)
//        slidesList.add(countOfSnacks)

        val cookingPreferences = CookingPreferencesFragment(
            form,
            productArraysNames.count() + 3,
            this
        )
        cookingPreferences.setPrev(slidesList.last() as Fragment)
        slidesList.add(cookingPreferences)

        val finalSettingsOfForm = FinalSettingsOfFormFragment(
            form,
            productArraysNames.count() + 3,
            this,
            editCpfcFragment
        )
        slidesList.add(finalSettingsOfForm)
        cookingPreferences.setNext(finalSettingsOfForm)

        makeCurrentFragment(personalFragment)
    }

    private fun sendCreateFormRequest(form: Form) {
        progressBar.visibility = View.VISIBLE
        val jsonForm = JsonConverter.formToJson(form)
        jsonForm.put("email", user.email)
        val params: MutableMap<Any?, Any?> = HashMap()
        //Set data
        params["userId"] = user.userId
        params["profile"] = jsonForm
        val payload = JSONObject(params)
        val str = payload.toString()
        val responseHandler: (JSONObject) -> Unit = { response: JSONObject ->
            Log.e("msg", response.toString())
            if (response.getString("error") != "") {
                if (response.getString("error") == "not valid ingredients") {
                    val msgs = response.getJSONArray("mealErrors")
                    val newSequence = ArrayList<QuestionsFragment>()
                    for (i in 0 until msgs.length()) {
                        var currMsg =
                            msgs.getString(i).split(":")[1].trim().trim { char -> char == '"' }
                        currMsg = currMsg.replace(
                            "Какие сладости вы любите?",
                            "От чего вы не готовы отказаться?"
                        )
                        val fragmentIndex = questionsArray.indexOf(currMsg)
                        val frag = slidesList[fragmentIndex + 1]
                        if (frag is QuestionsFragment) {
                            newSequence.add(frag)
                            when (i) {
                                0 -> {
                                    frag.prevFragment = slidesList[0] as Fragment
                                    (slidesList[0] as PersonalFormFragment).nextFragment = frag
                                }
                                msgs.length() - 1 -> {
                                    frag.nextFragment = slidesList.last() as Fragment
                                    newSequence[i - 1].nextFragment = frag
                                    (slidesList.last() as FinalSettingsOfFormFragment).prevFragment =
                                        frag
                                }
                                else -> {
                                    frag.prevFragment = newSequence[i - 1]
                                    newSequence[i - 1].nextFragment = frag
                                }
                            }
                        } else {
                            Log.e("plan error", currMsg)
                        }
                    }
                    val warningDialog = DialogWithoutButton(
                        "Недостаточное количество продуктов",
                        "Выберите больше продуктов в предоставленных категориях"
                    )
                    makeCurrentFragment(newSequence[0])
                    showDialog(warningDialog)
                }
            } else {
                val plan = Plan(response.getJSONObject("plan"))
                if (!form.hasPlan) {
                    user.formsWithoutPlan.remove(form)
                    user.plans.add(plan)
                    (formsFragment.formsRecyclerView.adapter as? FormsAdapter)?.notifyInsertNewPlan()
                } else if (form.isActive) {
                    user.activePlan = plan
                    user.plans.removeIf {
                        it.form == form
                    }
                } else {
                    user.plans.removeIf {
                        it.form == form
                    }
                    plan.form = form
                    user.plans.add(plan)
                }
                makeCurrentFragment(formsFragment)
                if (user.activePlan == null) {
                    val dialog = DialogTwoButtons(
                        "Активировать анкету?",
                        "Для составления плана питания вам необходимо активировать заполненную анкету. Хотите сделать это сейчас?",
                        "Да",
                        "Нет",
                        {
                            onButtonClicked(plan.form, 0)
                        }
                    )
                    showDialog(dialog)
                }
            }
            progressBar.visibility = ProgressBar.GONE
        }
        val errorHandler: (VolleyError) -> Unit = { error: VolleyError ->
            val warningDialog = DialogWithoutButton(
                "Ошибка создания плана",
                "Во время создания плана возникла ошибка, пожалуйста проверьте свое интрент соединение и повторите попытку позже"
            )
            progressBar.visibility = ProgressBar.GONE
            showDialog(warningDialog)
            Log.e("Create new plan error", error.toString())
        }

        val request = RequestSender.getJsonRequestJsonResponse(
            RequestSender.CREATE_PLAN_URL,
            payload,
            responseHandler,
            errorHandler
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }

    internal fun createPlanByForm(form: Form) {
        if (form.name == "Незаконченная анкета") {
            val nameDialog = DialogWithEditText(
                form,
                "Название анкеты",
                "Название анкеты",
                "Сохранить",
                {
                    sendCreateFormRequest(form)
                }
            )
            showDialog(nameDialog)
        } else {
            sendCreateFormRequest(form)
        }
    }

    override fun onButtonClicked(form: Form, action: Int) {
        when (action) {
            0 -> {
                if (form.hasPlan && !form.isActive) {
                    if (!user.isSubscribed && !user.isFirstWeek)
                        showWarningAboutEndOfTrialWeek()
                    else {
                        val plan = user.plans.find { plan -> plan.form == form }
                        user.activePlan = plan
                        AchievementChecker.checkAchievements()
//                    bottomNavigation?.menu?.getItem(2)?.isChecked = true
//                    makeCurrentFragment(fragmentWithPlan)
                    }
                } else if (form.isActive) {
                    user.activePlan = null
                }
                if (user.plans.isEmpty() && user.activePlan == null)
                    formsFragment.setNewWarningMsg("Для заполнения анкеты нажмите на кнопку \"Новая анкета\" и укажите все необходимые данные, после чего нажмите на кнопку \"Сохранить анкету\"")
                else if (user.activePlan == null)
                    formsFragment.setNewWarningMsg("Для составления плана вам нужно выбрать одну из законченных анкет и составить по ней план")
                else
                    formsFragment.setNewWarningMsg(
                        "Над кнопкой \"Новая анкета\" располагается анкета, по которой строится текущий план питания.\n" +
                                "Чтобы создать новый план питания по другой анкете, нажмите на нее и нажмите на кнопку \"Составить план\""
                    )
                formsFragment.formsRecyclerView.adapter?.notifyDataSetChanged()
                CoroutineScope(Dispatchers.IO).launch {
                    DataUpdater.updateAllPlans(userDataFile, user)
                }
            }
            1 -> {
                updateForm(form, !form.hasPlan)
                CoroutineScope(Dispatchers.IO).launch {
                    DataUpdater.updateAllPlans(userDataFile, user)
                }
            }
            2 -> {
                if (form.hasPlan && !form.isActive) {
                    val plan = user.plans.find { plan -> plan.form == form }
                    if (plan != null)
                        user.plans.add(plan.copy())
                    formsFragment.formsRecyclerView.adapter?.notifyDataSetChanged()
                } else if (form.hasPlan && form.isActive) {
                    val copyPlan = user.activePlan!!.copy()
                    user.plans.add(user.activePlan!!.copy())
                    formsFragment.formsRecyclerView.adapter?.notifyDataSetChanged()
                } else if (!form.hasPlan) {
                    user.formsWithoutPlan.add(form.copy())
                    formsFragment.notCompleteFormsRecyclerView.adapter?.notifyDataSetChanged()
                }
                CoroutineScope(Dispatchers.IO).launch {
                    DataUpdater.updateAllPlans(userDataFile, user)
                }
            }
            3 -> {
                val dialog = DialogTwoButtons(
                    "Удаление анкеты",
                    "Вы уверены, что хотите удалить, анкету \"${form.name}\"? После удаления анкету невозможно будет восстановить",
                    "Да",
                    "Нет",
                    { _ ->
                        when {
                            form.isActive -> {
                                user.activePlan = null
                                val plan = user.plans.find { plan -> plan.form == form }
                                user.plans.remove(plan)
                                formsFragment.formsRecyclerView.adapter?.notifyDataSetChanged()
                                if (user.plans.isEmpty() && user.activePlan == null)
                                    formsFragment.setNewWarningMsg("Для заполнения анкеты нажмите на кнопку \"Новая анкета\" и укажите все необходимые данные, после чего нажмите на кнопку \"Сохранить анкету\"")
                                else if (user.activePlan == null)
                                    formsFragment.setNewWarningMsg("Для составления плана вам нужно выбрать одну из законченных анкет и составить по ней план")
                                else
                                    formsFragment.setNewWarningMsg(
                                        "Над кнопкой \"Новая анкета\" располагается анкета, по которой строится текущий план питания.\n" +
                                                "Чтобы создать новый план питания по другой анкете, нажмите на нее и нажмите на кнопку \"Составить план\""
                                    )
                            }
                            form.hasPlan -> {
                                val plan = user.plans.find { plan -> plan.form == form }
                                user.plans.remove(plan)
                                formsFragment.formsRecyclerView.adapter?.notifyDataSetChanged()
                                if (user.plans.isEmpty() && user.activePlan == null)
                                    formsFragment.setNewWarningMsg("Для заполнения анкеты нажмите на кнопку \"Новая анкета\" и укажите все необходимые данные, после чего нажмите на кнопку \"Сохранить анкету\"")
                                else if (user.activePlan == null)
                                    formsFragment.setNewWarningMsg("Для составления плана вам нужно выбрать одну из законченных анкет и составить по ней план")
                                else
                                    formsFragment.setNewWarningMsg(
                                        "Над кнопкой \"Новая анкета\" располагается анкета, по которой строится текущий план питания.\n" +
                                                "Чтобы создать новый план питания по другой анкете, нажмите на нее и нажмите на кнопку \"Составить план\""
                                    )
                            }
                            else -> {
                                user.formsWithoutPlan.remove(form)
                                formsFragment.notCompleteFormsRecyclerView.adapter?.notifyDataSetChanged()
                                if (user.formsWithoutPlan.isEmpty())
                                    formsFragment.notCompleteFormsHeader.visibility = View.GONE
                            }
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            DataUpdater.updateAllPlans(userDataFile, user)
                        }
                    }
                )
                showDialog(dialog)
            }
        }
    }

    override fun userGetAchievement(achievement: Achievement) {
        val dialog = DialogImage(
            achievement.image,
            "Новое достижение",
            achievement.name,
            achievement.description
        )
        showDialog(dialog)
    }

    internal fun logOut() {
        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (dir != null)
            DataUpdater.saveExtraInfoAboutUser(user, dir)
        userDataFile.delete()
        finish()
    }

    override fun onPause() {
        CoroutineScope(Dispatchers.IO).launch {
            if (userDataFile.exists()) {
                DataUpdater.updateAchievements(userDataFile, user)
                DataUpdater.updateAllPlans(userDataFile, user)
                DataUpdater.updateOtherInfo(userDataFile, user)
            }
        }
        super.onPause()
    }
}