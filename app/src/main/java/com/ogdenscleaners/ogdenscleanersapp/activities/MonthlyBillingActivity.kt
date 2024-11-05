package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ogdenscleaners.ogdenscleanersapp.adapters.BillingAdapter
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityMonthlyBillingBinding
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement
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

        PaymentConfiguration.init(applicationContext, "your_publishable_key_here")
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        billingAdapter = BillingAdapter(mutableListOf()) { billingStatement, isSelected ->
            billingViewModel.markStatementsPaid(listOf(billingStatement))
        }

        binding.recyclerViewBilling.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewBilling.adapter = billingAdapter

        billingViewModel.loadBillingStatements()

        billingViewModel.billingStatements.observe(this, Observer {
            billingAdapter.updateBillingStatements(it)
        })

        billingViewModel.paymentIntentClientSecret.observe(this, Observer { clientSecret ->
            clientSecret?.let {
                paymentSheet.presentWithPaymentIntent(
                    it,
                    PaymentSheet.Configuration("Ogden's Cleaners"),
                )
            }
        })

        binding.button5.setOnClickListener {
            val selectedStatements = billingAdapter.getSelectedBillingStatements()
            if (selectedStatements.isNotEmpty()) {
                billingViewModel.initiatePayment(selectedStatements) { error ->
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please select at least one billing statement to make a payment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
                billingViewModel.markStatementsPaid(billingAdapter.getSelectedBillingStatements())
            }
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Payment canceled.", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Payment failed: ${paymentSheetResult.error.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
