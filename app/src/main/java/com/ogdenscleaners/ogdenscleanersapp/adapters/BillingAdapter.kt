package com.ogdenscleaners.ogdenscleanersapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.databinding.ItemBillingStatementBinding
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement

class BillingAdapter(
    private val billingList: MutableList<BillingStatement>,
    private val onItemSelected: (BillingStatement, Boolean) -> Unit
) : RecyclerView.Adapter<BillingAdapter.BillingViewHolder>() {

    private val selectedStatements = mutableSetOf<BillingStatement>()

    /**
     * Retrieve the selected billing statements.
     */
    fun getSelectedBillingStatements(): List<BillingStatement> = selectedStatements.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        val binding = ItemBillingStatementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val billingStatement = billingList[position]
        holder.bind(billingStatement)
    }

    override fun getItemCount(): Int = billingList.size

    /**
     * Update the list of billing statements and notify the adapter to refresh the data.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateBillingStatements(newStatements: List<BillingStatement>) {
        billingList.clear()
        billingList.addAll(newStatements)
        notifyDataSetChanged()
    }

    inner class BillingViewHolder(private val binding: ItemBillingStatementBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind the data to the view for the billing statement.
         */
        @SuppressLint("SetTextI18n")
        fun bind(billingStatement: BillingStatement) {
            // Display billing statement data
            binding.textStatementDate.text = billingStatement.date
            binding.textAmountOwed.text = "$${billingStatement.amountOwed}"

            // Set up the checkbox
            binding.checkbox.setOnCheckedChangeListener(null) // Clear any previous listeners
            binding.checkbox.isChecked = selectedStatements.contains(billingStatement)

            // Disable the checkbox for already paid statements
            binding.checkbox.isEnabled = !billingStatement.paidStatus

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (billingStatement.paidStatus) {
                    // Show a toast if the statement is already paid
                    Toast.makeText(
                        binding.root.context,
                        "${billingStatement.date} is already paid.",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.checkbox.isChecked = false
                } else {
                    // Add or remove the billing statement from selected statements
                    if (isChecked) {
                        selectedStatements.add(billingStatement)
                    } else {
                        selectedStatements.remove(billingStatement)
                    }
                    onItemSelected(billingStatement, isChecked)
                }
            }
        }
    }
}
