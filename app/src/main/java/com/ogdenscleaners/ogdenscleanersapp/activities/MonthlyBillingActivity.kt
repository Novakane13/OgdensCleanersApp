package com.ogdenscleaners.ogdenscleanersapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.adapters.BillingAdapter
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement
import com.ogdenscleaners.ogdenscleanersapp.models.CreditCard
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONObject

class MonthlyBillingActivity : AppCompatActivity() {
    private lateinit var billingRecyclerView: RecyclerView
    private lateinit var payBillButton: Button
    private lateinit var billingAdapter: BillingAdapter
    private lateinit var paymentSheet: PaymentSheet
    private var paymentIntentClientSecret: String? = null

    private val billingStatements = mutableListOf<BillingStatement>()
    private val savedCards = mutableListOf<CreditCard>() // Placeholder for saved credit cards

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_billing)

        // Initialize PaymentConfiguration with the test key
        PaymentConfiguration.init(applicationContext, "pk_test_51QEmC6F9q8Y1A3UES8uzimDczaKS3xMRUNr9QN4vhQN8wjktGMEONNrWWP7mFCJRrdYDmTPADDDVxn1GvS0mTkCw00XlEDwkSY")

        // Initialize views
        billingRecyclerView = findViewById(R.id.recyclerViewBilling)
        payBillButton = findViewById(R.id.button5)

        // Initialize Stripe PaymentSheet
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        // Set up RecyclerView and Adapter
        billingAdapter = BillingAdapter(billingStatements) { billingStatement, isSelected ->
            if (isSelected) {
                billingStatements.add(billingStatement)
            } else {
                billingStatements.remove(billingStatement)
            }
        }

        billingRecyclerView.layoutManager = LinearLayoutManager(this)
        billingRecyclerView.adapter = billingAdapter

        // Add placeholder billing statements for testing
        billingStatements.addAll(
            listOf(
                BillingStatement("1", "January 2024", "100.00", "Monthly dry cleaning charges", "Billing statement for the Month of January", false),
                BillingStatement("2", "February 2024", "80.00", "Monthly dry cleaning charges","Billing Statement for the Month of February", true)
            )
        )
        billingAdapter.notifyDataSetChanged()

        // Set up onClickListener for Pay Bill button
        payBillButton.setOnClickListener {
            val selectedStatements = billingAdapter.getSelectedBillingStatements()
            if (selectedStatements.isNotEmpty()) {
                showPaymentOptions(selectedStatements)
            } else {
                Toast.makeText(this, "Please select at least one billing statement to make a payment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPaymentOptions(selectedStatements: List<BillingStatement>) {
        // Show a dialog to let the user select between saved cards or enter a new one
        val options = arrayOf("Use Saved Card", "Enter New Card")
        AlertDialog.Builder(this)
            .setTitle("Choose Payment Method")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> selectSavedCardForPayment(selectedStatements)
                    1 -> initiateNewCardPayment(selectedStatements)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun selectSavedCardForPayment(selectedStatements: List<BillingStatement>) {
        if (savedCards.isNotEmpty()) {
            val cardOptions = savedCards.map { "${it.cardholderName} - **** ${it.lastFourDigits}" }.toTypedArray()
            AlertDialog.Builder(this)
                .setTitle("Select a Saved Card")
                .setItems(cardOptions) { _, which ->
                    // Assume we use the selected card and initiate payment with its information
                    val selectedCard = savedCards[which]
                    initiatePayment(selectedStatements, selectedCard)
                }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            Toast.makeText(this, "No saved cards found. Please add a card first.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initiateNewCardPayment(selectedStatements: List<BillingStatement>) {
        // Start the payment flow with new card entry through Stripe PaymentSheet
        initiatePayment(selectedStatements, null)
    }

    private fun initiatePayment(selectedStatements: List<BillingStatement>, creditCard: CreditCard?) {
        // Placeholder for calling server to create PaymentIntent
        val totalAmount = calculateTotalAmount(selectedStatements)
        createPaymentIntent(totalAmount)
    }

    private fun calculateTotalAmount(selectedStatements: List<BillingStatement>): Int {
        // Calculate the total amount in cents
        return selectedStatements.sumOf { it.totalAmount.toDouble() * 100 }.toInt()
    }

    private fun createPaymentIntent(amount: Int) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:4242/create-payment-intent"

        val jsonObject = JSONObject().apply {
            put("amount", amount)
            put("currency", "usd")
        }

        val request = JsonObjectRequest(Request.Method.POST, url, jsonObject, { response ->
            paymentIntentClientSecret = response.getString("clientSecret")
            paymentIntentClientSecret?.let { presentPaymentSheet(it) }
        }, { error ->
            Log.e("MonthlyBillingActivity", "Error creating payment intent: $error")
            Toast.makeText(this, "Error creating payment intent", Toast.LENGTH_SHORT).show()
        })
        queue.add(request)
    }

    private fun presentPaymentSheet(clientSecret: String) {
        paymentSheet.presentWithPaymentIntent(
            clientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Ogden's Cleaners"
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
                // Mark billing statements as paid
                val selectedBillingStatements = billingAdapter.getSelectedBillingStatements()
                selectedBillingStatements.forEach { it.paidStatus = true }
                billingAdapter.notifyDataSetChanged() // Update billing list
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
