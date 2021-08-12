package com.example.smarteat.vh

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.smarteat.R

class NotBoughtProductViewHolder(itemView: View) : ProductViewHolder(itemView) {
    override val productName: TextView = itemView.findViewById(R.id.not_bought_product_row__form_name)
    override val weight: TextView = itemView.findViewById(R.id.not_bought_product_row__weight)
    override val productCard: CardView = itemView.findViewById(R.id.not_bought_product_row__product_card)
    override val radioButton: RadioButton = itemView.findViewById(R.id.not_bought_product_row__radio_button)
}