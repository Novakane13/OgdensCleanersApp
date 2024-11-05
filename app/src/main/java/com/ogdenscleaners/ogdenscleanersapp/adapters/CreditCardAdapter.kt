package com.ogdenscleaners.ogdenscleanersapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.databinding.ItemCreditCardBinding
import com.ogdenscleaners.ogdenscleanersapp.models.Customer

class CreditCardAdapter(
    private var cards: MutableList<Customer.CreditCard>
) : RecyclerView.Adapter<CreditCardAdapter.CreditCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val binding = ItemCreditCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.size

    fun updateCards(newCards: List<Customer.CreditCard>) {
        cards.clear()
        cards.addAll(newCards)
        notifyDataSetChanged()
    }

    inner class CreditCardViewHolder(private val binding: ItemCreditCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: Customer.CreditCard) {
            binding.cardholderName.text = card.cardholderName
            binding.cardLastFourDigits.text = "**** **** **** ${card.lastFourDigits}"
            binding.cardExpiry.text = "Exp: ${card.expirationDate}"
        }
    }
}
