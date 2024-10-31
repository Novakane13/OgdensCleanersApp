package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.adapters.BillingAdapter
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement
import com.ogdenscleaners.ogdenscleanersapp.models.CreditCard
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONArray

class MonthlyBillingActivity : AppCompatActivity() {

    private lateinit var billingRecyclerView: RecyclerView
    private lateinit var makePaymentButton: MaterialButton
    private lateinit var billingAdapter: BillingAdapter
    private lateinit var paymentSheet: PaymentSheet
    private var paymentIntentClientSecret: String? = null
    private val billingStatements = mutableListOf<BillingStatement>()
    private val savedCards: MutableList<CreditCard> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_billing)

        // Initialize views
        billingRecyclerView = findViewById(R.id.billingRecyclerView)
        makePaymentButton = findViewById(R.id.makePaymentButton)

        // Initialize Stripe PaymentSheet
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        // Set up RecyclerView and Adapters
        billingAdapter = BillingAdapter(onMoreInfoClick = { statement -> showBillingDetails(statement) })

        billingRecyclerView.layoutManager = LinearLayoutManager(this)
        billingRecyclerView.adapter = billingAdapter

        // Add placeholder billing statements
        billingStatements.addAll(
            listOf(
                BillingStatement("January", "2024-01-31", 100.0, "Unpaid"),
                BillingStatement("February", "2024-02-28", 150.0, "Unpaid"),
                BillingStatement("March", "2024-03-31", 120.0, "Unpaid")
            )
        )
        billingAdapter.submitList(billingStatements)

        // Load saved credit cards
        loadSavedCreditCards()

        // Set up onClickListeners
        makePaymentButton.setOnClickListener {
            val selectedStatements = billingAdapter.getSelectedStatements()
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

        // Create payment data and make network call to initiate PaymentIntent
        // Assume this function communicates with the backend and returns a PaymentIntent's client secret
        paymentIntentClientSecret = "retrieved_client_secret_here" // Placeholder for actual client secret
        paymentIntentClientSecret?.let { presentPaymentSheet(it) }
    }

    private fun presentPaymentSheet(clientSecret: String) {
        paymentSheet.presentWithPaymentIntent(
            clientSecret, PaymentSheet.Configuration(
                merchantDisplayName = "Ogden's Cleaners"
            )
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
                val selectedStatements = billingAdapter.getSelectedStatements()
                billingStatements.removeAll(selectedStatements)
                billingAdapter.clearSelectedStatements()
                billingAdapter.notifyDataSetChanged()
            }
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Payment canceled.", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Payment failed: ${paymentSheetResult.error.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadSavedCreditCards() {
        val cardsJson = getSharedPreferences("credit_cards", MODE_PRIVATE).getString("cards", null)
        if (cardsJson != null) {
            val jsonArray = JSONArray(cardsJson)
            for (i in 0 until jsonArray.length()) {
                val cardJson = jsonArray.getJSONObject(i)
                val card = CreditCard(
                    cardholderName = cardJson.getString("cardholderName"),
                    lastFourDigits = cardJson.getString("lastFourDigits"),
                    expiryDate = cardJson.getString("expiryDate")
                )
                savedCards.add(card)
            }
        }
    }

    private fun calculateTotalAmount(statements: List<BillingStatement>): Int {
        return statements.sumOf { (it.amountDue * 100).toInt() }
    }
}
