package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.adapters.BillingAdapter
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityMonthlyBillingBinding
import com.ogdenscleaners.ogdenscleanersapp.viewmodel.BillingViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthlyBillingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonthlyBillingBinding
    private val billingViewModel: BillingViewModel by viewModels()
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var billingAdapter: BillingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyBillingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PaymentConfiguration.init(applicationContext, getString(R.string.stripe_publishable_key))
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        setupRecyclerView()
        observeViewModel()

        // Handle Pay Bill button click
        binding.buttonPayBill.setOnClickListener {
            val selectedStatements = billingAdapter.getSelectedBillingStatements()
            if (selectedStatements.isNotEmpty()) {
                // Obtain customerId and paymentMethodId
                val customerId = getCustomerId()
                val paymentMethodId = getPaymentMethodId()

                billingViewModel.initiatePayment(selectedStatements, customerId, paymentMethodId)
                    .observe(this) { result ->
                        result.onSuccess { clientSecret ->
                            paymentSheet.presentWithPaymentIntent(
                                clientSecret,
                                PaymentSheet.Configuration("Ogden's Cleaners")
                            )
                        }.onFailure { error ->
                            Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please select a statement to pay.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle View Billing Statement button click
        binding.buttonViewBillingStatement.setOnClickListener {
            val selectedStatement = billingAdapter.getSelectedBillingStatements().firstOrNull()
            if (selectedStatement != null) {
                val intent = Intent(this, DetailedBillingStatementActivity::class.java).apply {
                    putExtra("statement_id", selectedStatement.statementId)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a statement to view.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        billingAdapter = BillingAdapter(mutableListOf()) { billingStatement, isSelected ->
            // Optional: Handle real-time selection UI updates if needed
        }
        binding.recyclerViewBilling.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewBilling.adapter = billingAdapter
    }

    private fun observeViewModel() {
        billingViewModel.loadBillingStatements()
        billingViewModel.billingStatements.observe(this) { statements ->
            billingAdapter.updateBillingStatements(statements)
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
                billingViewModel.markStatementsPaid(billingAdapter.getSelectedBillingStatements())
            }
            is PaymentSheetResult.Canceled -> Toast.makeText(this, "Payment canceled.", Toast.LENGTH_SHORT).show()
            is PaymentSheetResult.Failed -> Toast.makeText(
                this,
                "Payment failed: ${paymentSheetResult.error.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Helper method to get customer ID
    private fun getCustomerId(): String {
        // TODO: Implement logic to retrieve the customer ID from user session or preferences
        return "cus_123456789" // Replace with actual customer ID
    }

    // Helper method to get payment method ID
    private fun getPaymentMethodId(): String {
        // TODO: Implement logic to create or retrieve a payment method ID using Stripe SDK
        // For example, you might prompt the user to enter payment details and create a PaymentMethod
        return "pm_123456789" // Replace with actual payment method ID
    }
}
