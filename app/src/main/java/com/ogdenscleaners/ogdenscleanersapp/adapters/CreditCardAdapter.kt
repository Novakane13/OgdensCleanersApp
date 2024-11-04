package com.ogdenscleaners.ogdenscleanersapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.models.Customer.CreditCard

class CreditCardAdapter(private val cards: MutableList<CreditCard>) :
    RecyclerView.Adapter<CreditCardAdapter.CreditCardViewHolder>() {

    class CreditCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardholderName: TextView = view.findViewById(R.id.cardholder_name)
        val lastFourDigits: TextView = view.findViewById(R.id.last_four_digits)
        val expiryDate: TextView = view.findViewById(R.id.expiry_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return CreditCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        val card = cards[position]
        holder.cardholderName.text = card.cardholderName
        holder.lastFourDigits.text = "**** **** **** ${card.lastFourDigits}"
        holder.expiryDate.text = card.expirationDate
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    fun addCard(card: CreditCard) {
        cards.add(card)
        notifyItemInserted(cards.size - 1)
    }
}
