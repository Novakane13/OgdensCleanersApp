package com.ogdenscleaners.ogdenscleanersapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement
import android.widget.Toast


class BillingAdapter(private val billingList: List<BillingStatement>) :
    RecyclerView.Adapter<BillingAdapter.BillingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_billing_statement, parent, false)
        return BillingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val billingStatement = billingList[position]
        holder.bind(billingStatement)
    }

    override fun getItemCount(): Int {
        return billingList.size
    }

    inner class BillingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val monthTextView: TextView = itemView.findViewById(R.id.monthTextView)
        private val totalAmountTextView: TextView = itemView.findViewById(R.id.totalAmountTextView)
        private val detailsButton: Button = itemView.findViewById(R.id.detailsButton)
        private val payButton: Button = itemView.findViewById(R.id.payButton)

        @SuppressLint("SetTextI18n")
        fun bind(billingStatement: BillingStatement) {
            monthTextView.text = billingStatement.month
            totalAmountTextView.text = billingStatement.totalAmount
            detailsButton.setOnClickListener {
                // For now, just show the details in a Toast (you can replace this with a new screen later)
                Toast.makeText(itemView.context, billingStatement.details, Toast.LENGTH_SHORT).show()
            }

            payButton.setOnClickListener {
                if (!billingStatement.paidStatus) {
                    Toast.makeText(itemView.context, "Proceed to payment for ${billingStatement.month}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(itemView.context, "${billingStatement.month} is already paid.", Toast.LENGTH_SHORT).show()
                }
            }

            // Optionally, disable the pay button if the statement is already paid
            if (billingStatement.paidStatus) {
                payButton.isEnabled = false
                payButton.text = "Paid"
            }
        }
    }
}
