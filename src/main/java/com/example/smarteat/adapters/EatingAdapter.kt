package com.example.smarteat.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarteat.R
import com.example.smarteat.models.Eating
import com.example.smarteat.ui.dialogs.DialogOneButton
import com.example.smarteat.ui.fragments.FragmentWithPlan
import java.util.*
import kotlin.collections.ArrayList

class EatingAdapter(
    private val eating: ArrayList<Eating>,
    private val parentFragment: FragmentWithPlan
) : RecyclerView.Adapter<EatingAdapter.EatingViewHolder>() {

    inner class EatingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.eating_row__eating_name)
        val recipeNameTextView: TextView = itemView.findViewById(R.id.eating_row__recipe_name)
        val line : View = itemView.findViewById(R.id.eating_row__line)
        val arrowIcon : ImageView = itemView.findViewById(R.id.eating_row__main_arrow)
        val expandedProperties : CardView = itemView.findViewById(R.id.eating_row__expanded_properties)
        val ingredients : RecyclerView = itemView.findViewById(R.id.eating_row__ingredients_list)
        val properties: TextView = itemView.findViewById(R.id.eating_row__properties)
        val headerWrapper: ViewGroup = itemView.findViewById(R.id.eating_row__header_wrapper)
        val view: View = itemView
        val recipeCard: CardView = itemView.findViewById(R.id.eating_row__recipe_card)

        init {
            val clickListener = View.OnClickListener { _ ->
                val e = eating[adapterPosition]
                e.isExpanded = !e.isExpanded
                notifyItemChanged(adapterPosition)
            }

            val recipeCardOnClickListener = View.OnClickListener {
                val e = eating[adapterPosition]
                val recipes = parentFragment.user.activePlan!!.recipes[e.recipeNum / 100 - 1]
                val recipePositions = ArrayList<Pair<Int, Int>>()
                for (recipe in e.recipes) {
                    val index = recipes.indexOf(recipe)
                        recipePositions.add(Pair(e.recipeNum / 100 - 1, index))
                }
                if (recipePositions.isNotEmpty())
                    parentFragment.parentActivity.showRecipe(recipePositions, e.recipeName)
                else {
                    val dialog = DialogOneButton(
                        "Нет рецепта",
                        "К сожалению, для выбранного вами блюда нет рецепта, так как его приготовление является достаточно простым и не требует описания.",
                        "Продолжить"
                    )
                    parentFragment.parentActivity.showDialog(dialog)
                }

            }
            recipeCard.setOnClickListener(recipeCardOnClickListener)
            ingredients.setOnClickListener(recipeCardOnClickListener)
            headerWrapper.setOnClickListener(clickListener)
            arrowIcon.setOnClickListener(clickListener)
            nameTextView.setOnClickListener(clickListener)
            ingredients.layoutManager = LinearLayoutManager(itemView.context)
        }
    }

    private fun translate(string: String): String =
        when (string) {
            "breakfast" -> "Завтрак"
            "dinner" -> "Обед"
            "snack1" -> "Перекус 1"
            "snack2" -> "Перекус 2"
            "snack3" -> "Перекус 3"
            "supper" -> "Ужин"
            else -> ""
        }

    override fun getItemViewType(position: Int): Int {
        return R.layout.eating_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EatingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        return EatingViewHolder(view)
    }

    override fun onBindViewHolder(holder: EatingViewHolder, position: Int) {
        val currEating = eating[position]

        holder.nameTextView.text = translate(currEating.name)
        holder.properties.text = currEating.stringProperties
        holder.recipeNameTextView.text = "${currEating.recipeName}:"
        holder.ingredients.adapter = IngredientsAdapter(currEating.ingredients, parentFragment, currEating)

        if (position == eating.size - 1)
            holder.line.visibility = View.GONE

        val isExpanded = currEating.isExpanded
        holder.arrowIcon.rotationX = if (isExpanded) 180F else 0F
        holder.expandedProperties.visibility = if (isExpanded) View.VISIBLE else View.GONE

    }

    override fun getItemCount(): Int = eating.size
}