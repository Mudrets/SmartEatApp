package com.example.smarteat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.R.*
import com.example.smarteat.adapters.ProductCategoriesAdapter
import com.example.smarteat.models.Ingredient
import com.example.smarteat.models.User
import com.example.smarteat.utils.DataUpdater
import com.example.smarteat.utils.ProductsHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate


/**
 * A simple [Fragment] subclass.
 * Use the [ProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductsFragment(
    private val user: User,
    private val userFile: File,
    private val productsArrayNames: ArrayList<Int>
) : Fragment() {
    private val categories = ArrayList<String>()
    private lateinit var categoriesRecyclerView: RecyclerView
    private val displayedElements = ArrayList<ProductsHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(layout.fragment_products, container, false)
        categories.addAll(resources.getStringArray(R.array.categories_names))
        if (user.activePlan != null && (user.productsForWeek == null ||
                    user.updatedProductsForWeekAt == null ||
                    user.numOfWeekProducts != user.activePlan!!.numOfWeek)
        ) {
            distributeWeekProducts()
        }

        if (user.activePlan == null)
            user.productsForWeek = null

        categoriesRecyclerView = view.findViewById(R.id.fragment_products__categories_list)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(context)
        categoriesRecyclerView.isNestedScrollingEnabled = false
        if (user.productsForWeek == null)
            categoriesRecyclerView.adapter = ProductCategoriesAdapter()
        else {
            categoriesRecyclerView.adapter = ProductCategoriesAdapter(user.productsForWeek!!)
        }
        return view
    }

    override fun onPause() {
        super.onPause()
        CoroutineScope(Dispatchers.IO).launch {
            DataUpdater.updateAllPlans(userFile, user)
        }
    }

    private fun distributeWeekProducts() {
        val allProducts = user.activePlan?.productsForWeek ?: ArrayList()
        val distributeForAllCategories = HashMap<Int, ArrayList<Ingredient>>()
        for (productsId in productsArrayNames) {
            distributeForAllCategories[productsId] = ArrayList()
            val productArray = resources.getStringArray(productsId)
            val productSize = allProducts.size
            var index = 0
            for (i in 0 until productSize) {
                if (productArray.contains(allProducts[index].nameOfQuiz)) {
                    distributeForAllCategories[productsId]!!.add(allProducts[index])
                    allProducts.remove(allProducts[index--])
                }
                index++
            }
        }
        val productsForEachCategory = ArrayList<ProductsHolder>()
        var arrayList = combineProductsLists(
            distributeForAllCategories[productsArrayNames[0]] ?: ArrayList(),
            distributeForAllCategories[productsArrayNames[1]] ?: ArrayList(),
            distributeForAllCategories[productsArrayNames[7]] ?: ArrayList()
        )
        productsForEachCategory.add(ProductsHolder(categories[0], arrayList))
        productsForEachCategory.add(
            ProductsHolder(
                categories[1],
                distributeForAllCategories[productsArrayNames[4]] ?: ArrayList()
            )
        )
        productsForEachCategory.add(
            ProductsHolder(
                categories[2],
                distributeForAllCategories[productsArrayNames[2]] ?: ArrayList()
            )
        )
        productsForEachCategory.add(
            ProductsHolder(
                categories[3],
                distributeForAllCategories[productsArrayNames[3]] ?: ArrayList()
            )
        )
        arrayList = combineProductsLists(
            distributeForAllCategories[productsArrayNames[5]] ?: ArrayList(),
            distributeForAllCategories[productsArrayNames[6]] ?: ArrayList()
        )
        productsForEachCategory.add(ProductsHolder(categories[4], arrayList))
        productsForEachCategory.add(
            ProductsHolder(
                categories[5],
                distributeForAllCategories[productsArrayNames[11]] ?: ArrayList()
            )
        )
        arrayList = combineProductsLists(
            distributeForAllCategories[productsArrayNames[9]] ?: ArrayList(),
            distributeForAllCategories[productsArrayNames[16]] ?: ArrayList()
        )
        productsForEachCategory.add(ProductsHolder(categories[6], arrayList))
        arrayList = combineProductsLists(
            distributeForAllCategories[productsArrayNames[13]] ?: ArrayList(),
            distributeForAllCategories[productsArrayNames[14]] ?: ArrayList()
        )
        productsForEachCategory.add(ProductsHolder(categories[7], arrayList))
        arrayList = combineProductsLists(
            distributeForAllCategories[productsArrayNames[10]] ?: ArrayList(),
            distributeForAllCategories[productsArrayNames[12]] ?: ArrayList(),
            distributeForAllCategories[productsArrayNames[15]] ?: ArrayList(),
            distributeForAllCategories[productsArrayNames[8]] ?: ArrayList()
        )
        productsForEachCategory.add(ProductsHolder(categories[8], arrayList))
        user.productsForWeek = productsForEachCategory
    }

    private fun combineProductsLists(vararg lists: ArrayList<Ingredient>): ArrayList<Ingredient> {
        val result = ArrayList<Ingredient>();
        for (list in lists)
            for (ingredient in list)
                result.add(ingredient)
        return result
    }

    private fun moreThanOneWeek(date: LocalDate): Boolean {
        var activateDate = user.activePlan!!.activatedAt!!
        val numOfWeek = user.activePlan!!.numOfWeek
        activateDate = activateDate.plusWeeks((numOfWeek - 1).toLong())
        return date.plusWeeks(1) < activateDate
    }
}