package com.ogdenscleaners.ogdenscleanersapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.databinding.CardItemBinding
import com.ogdenscleaners.ogdenscleanersapp.models.AppCustomer

class CreditCardAdapter(
    private var cards: MutableList<AppCustomer.CreditCard>
) : RecyclerView.Adapter<CreditCardAdapter.CreditCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.size

    fun updateCards(newCards: List<AppCustomer.CreditCard>) {
        cards.clear()
        cards.addAll(newCards)
        notifyDataSetChanged()
    }

    inner class CreditCardViewHolder(private val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: AppCustomer.CreditCard) {
            binding.cardholderName.text = card.cardholderName
            binding.lastFourDigits.text = itemView.context.getString(
                R.string.credit_card_display,
                card.lastFourDigits
            )
            binding.expiryDate.text = itemView.context.getString(
                R.string.expiration_date,
                card.expirationDate
            )
        }
    }
}
