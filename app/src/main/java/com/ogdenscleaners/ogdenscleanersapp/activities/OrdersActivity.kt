package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.adapters.OrderAdapter
import com.ogdenscleaners.ogdenscleanersapp.models.CreditCard
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONArray

class OrdersActivity : AppCompatActivity() {

    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var payButton: MaterialButton
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var paymentSheet: PaymentSheet
    private var paymentIntentClientSecret: String? = null
    private val savedCards: MutableList<CreditCard> = mutableListOf()
    private val ordersList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        // Initialize views
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView)
        payButton = findViewById(R.id.payButton)

        // Initialize Stripe PaymentSheet
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        // Set up RecyclerView and Adapters
        orderAdapter = OrderAdapter(onOrderSelected = { order -> toggleOrderSelection(order) })

        ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        ordersRecyclerView.adapter = orderAdapter

        // Load sample orders (This can be replaced with actual data)
        loadOrders()

        // Load saved credit cards
        loadSavedCreditCards()

        // Set up onClickListeners
        payButton.setOnClickListener {
            val selectedOrders = orderAdapter.getSelectedOrders()
            if (selectedOrders.isNotEmpty()) {
                showPaymentOptions(selectedOrders)
            } else {
                Toast.makeText(this, "Please select at least one order to make a payment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadOrders() {
        ordersList.addAll(
            listOf(
                Order(id = 1, date = 11/5/25, items = "Dry Cleaning - 3 Shirts", total = 30.0),
                Order(id = 2, date = 11/5/25, items = "Laundry - 2 Pants", total = 20.0),
                Order(id = 3, date = 11/5/25, items = "Leather Jacket", total = 50.0)
            )
        )
        orderAdapter.submitList(ordersList)
    }

    private fun showPaymentOptions(selectedOrders: List<Order>) {
        // Show a dialog to let the user select between saved cards or enter a new one
        val options = arrayOf("Use Saved Card", "Enter New Card")
        AlertDialog.Builder(this)
            .setTitle("Choose Payment Method")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> selectSavedCardForPayment(selectedOrders)
                    1 -> initiateNewCardPayment(selectedOrders)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun selectSavedCardForPayment(selectedOrders: List<Order>) {
        if (savedCards.isNotEmpty()) {
            val cardOptions = savedCards.map { "${it.cardholderName} - **** ${it.lastFourDigits}" }.toTypedArray()
            AlertDialog.Builder(this)
                .setTitle("Select a Saved Card")
                .setItems(cardOptions) { _, which ->
                    // Assume we use the selected card and initiate payment with its information
                    val selectedCard = savedCards[which]
                    initiatePayment(selectedOrders, selectedCard)
                }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            Toast.makeText(this, "No saved cards found. Please add a card first.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initiateNewCardPayment(selectedOrders: List<Order>) {
        // Start the payment flow with new card entry through Stripe PaymentSheet
        initiatePayment(selectedOrders, null)
    }

    private fun initiatePayment(selectedOrders: List<Order>, creditCard: CreditCard?) {
        // Placeholder for calling server to create PaymentIntent
        val totalAmount = calculateTotalAmount(selectedOrders)

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
                val selectedOrders = orderAdapter.getSelectedOrders()
                ordersList.removeAll(selectedOrders)
                orderAdapter.clearSelectedOrders()
                orderAdapter.notifyDataSetChanged()
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

    private fun calculateTotalAmount(orders: List<Order>): Int {
        return orders.sumOf { (it.amountDue * 100).toInt() }
    }

    private fun toggleOrderSelection(order: Order) {
        order.isSelected = !order.isSelected
        orderAdapter.notifyDataSetChanged()
    }
}
