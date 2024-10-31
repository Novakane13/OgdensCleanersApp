package com.ogdenscleaners.ogdenscleanersapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement

class BillingAdapter(
    private val billingList: List<BillingStatement>,
    private val onItemSelected: (BillingStatement, Boolean) -> Unit
) : RecyclerView.Adapter<BillingAdapter.BillingViewHolder>() {

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
        private val detailsButton: TextView = itemView.findViewById(R.id.detailsButton)
        private val selectCheckBox: CheckBox = itemView.findViewById(R.id.checkBox)

        @SuppressLint("SetTextI18n")
        fun bind(billingStatement: BillingStatement) {
            monthTextView.text = billingStatement.month
            totalAmountTextView.text = billingStatement.totalAmount

            detailsButton.setOnClickListener {
                // Display the details in a Toast for now
                Toast.makeText(itemView.context, billingStatement.details, Toast.LENGTH_SHORT).show()
            }

            selectCheckBox.setOnCheckedChangeListener(null) // Clear previous listeners to avoid duplication
            selectCheckBox.isChecked = billingStatement.paidStatus

            selectCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (billingStatement.paidStatus && isChecked) {
                    // Prevent selecting a paid billing statement
                    Toast.makeText(
                        itemView.context,
                        "${billingStatement.month} is already paid.",
                        Toast.LENGTH_SHORT
                    ).show()
                    selectCheckBox.isChecked = false
                } else {
                    onItemSelected(billingStatement, isChecked)
                }
            }

            // Disable the checkbox if the statement is already paid
            selectCheckBox.isEnabled = !billingStatement.paidStatus
        }
    }
}
