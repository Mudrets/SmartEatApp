package com.example.smarteat.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.adapters.DayAdapter
import com.example.smarteat.models.User
import com.example.smarteat.ui.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val dataFile = File(dir, "userData.json")
        if (!dataFile.exists())
            this.finish()
        else {
            val dataFromFile = dataFile.readText()
            user = User(JSONObject(dataFromFile))
        }
        setContentView(R.layout.activity_main)
        //initialization
        bottomNavigation = findViewById(R.id.bottom_navigation);
        val planFragment = PlanFragment()
        val formsFragment = FormsFragment()
        val productsFragment = ProductsFragment()
        val profileFragment = ProfileFragment()
        val recipesFragment = RecipesFragment()
        val fragmentWithPlan = FragmentWithPlan(user)

        if (user.activePlan == null)
            makeCurrentFragment(planFragment)
        else {
            makeCurrentFragment(fragmentWithPlan)
        }
        bottomNavigation.menu.getItem(2).isChecked = true

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.products -> makeCurrentFragment(productsFragment)
                R.id.prof -> makeCurrentFragment(profileFragment)
                R.id.recipes -> makeCurrentFragment(recipesFragment)
                R.id.forms -> makeCurrentFragment(formsFragment)
                R.id.plan -> {
                    if (user.activePlan == null)
                        makeCurrentFragment(planFragment)
                    else {
                        makeCurrentFragment(fragmentWithPlan)
                    }
                }
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}