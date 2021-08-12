package com.example.smarteat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.adapters.RecipeAdapter
import com.example.smarteat.models.User

class RecipesFragment(
    private val user: User
) : Fragment() {
    private lateinit var recipesRecycler: RecyclerView
    internal var selectedRecipes: ArrayList<Pair<Int, Int>> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)

        if (user.activePlan != null) {
            recipesRecycler = view.findViewById(R.id.activity_recipes__recipes)
            recipesRecycler.layoutManager = LinearLayoutManager(context)

            val recipesList = user.activePlan!!.recipes
            for (recipeCategory in recipesList)
                for (recipe in recipeCategory)
                    recipe.isExpanded = false

            var scrollToPos = 0
            if (selectedRecipes.isNotEmpty()) {
                var min = 100
                for ((category, recipe) in selectedRecipes) {
                    recipesList[category][recipe].isExpanded = true
                    if (recipe < min)
                        min = recipe
                }
                var index = 0
                for (i in 0 until selectedRecipes[0].first)
                    index += recipesList[i].size + 1
                index += min + 1
                scrollToPos = index
                selectedRecipes.clear()
            }
            recipesRecycler.adapter = RecipeAdapter(recipesList)
            recipesRecycler.scrollToPosition(scrollToPos + 1)
        }
        return view
    }
}