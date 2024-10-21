package com.ogdenscleaners.ogdenscleanersapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement

class BillingStatementAdapter(
    private val billingStatements: List<com.ogdenscleaners.ogdenscleanersapp.activities.BillingStatement>,
    private val onPayClickListener: (BillingStatement) -> Unit,
    private val onDetailsClickListener: (BillingStatement) -> Unit
) : RecyclerView.Adapter<BillingStatementAdapter.BillingViewHolder>() {

    inner class BillingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val monthTextView: TextView = view.findViewById(R.id.monthTextView)
        val totalAmountTextView: TextView = view.findViewById(R.id.totalAmountTextView)
        val payButton: Button = view.findViewById(R.id.payButton)
        val detailsButton: Button = view.findViewById(R.id.detailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_billing_statement, parent, false)
        return BillingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val statement = billingStatements[position]
        val context = holder.itemView.context

        // Set the month text directly
        holder.monthTextView.text = statement.month

        // Use getString() from the context to properly format the total amount text
        val totalAmountText = context.getString(R.string.billing_statement_total, statement.totalAmount)
        holder.totalAmountTextView.text = totalAmountText

        // Set click listeners for Pay and Details buttons
        holder.payButton.setOnClickListener {
            onPayClickListener(statement)
        }

        holder.detailsButton.setOnClickListener {
            onDetailsClickListener(statement)
        }
    }

    override fun getItemCount() = billingStatements.size
}
