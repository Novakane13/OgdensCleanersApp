package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.adapters.OrdersAdapter
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONObject

class OrdersActivity : AppCompatActivity() {
    private lateinit var activeOrdersRecyclerView: RecyclerView
    private lateinit var inactiveOrdersRecyclerView: RecyclerView
    private lateinit var moreInfoButton: MaterialButton
    private lateinit var makePaymentButton: MaterialButton
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var inactiveOrdersAdapter: OrdersAdapter
    private lateinit var paymentSheet: PaymentSheet
    private var paymentIntentClientSecret: String? = null

    private val activeOrders = mutableListOf<Order>()
    private val inactiveOrders = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        // Initialize PaymentConfiguration with the test key
        PaymentConfiguration.init(applicationContext, "pk_test_51QEmC6F9q8Y1A3UES8uzimDczaKS3xMRUNr9QN4vhQN8wjktGMEONNrWWP7mFCJRrdYDmTPADDDVxn1GvS0mTkCw00XlEDwkSY")

        // Initialize views
        activeOrdersRecyclerView = findViewById(R.id.active_orders_recycler_view)
        inactiveOrdersRecyclerView = findViewById(R.id.inactive_orders_recycler_view)
        moreInfoButton = findViewById(R.id.moreInfoButton)
        makePaymentButton = findViewById(R.id.makePaymentButton)

        // Initialize Stripe PaymentSheet
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        // Set up RecyclerViews and Adapters
        ordersAdapter = OrdersAdapter(onMoreInfoClick = { order -> showOrderDetails(order) })
        inactiveOrdersAdapter = OrdersAdapter(onMoreInfoClick = { order -> showOrderDetails(order) })

        activeOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        activeOrdersRecyclerView.adapter = ordersAdapter

        inactiveOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        inactiveOrdersRecyclerView.adapter = inactiveOrdersAdapter

        // Add placeholder orders for testing
        activeOrders.addAll(
            listOf(
                Order("1", date = "January 5, 2024", items = listOf("Shirt", "Pants"), total = 20.0),
                Order("2", date = "February 10, 2024", items = listOf("Jacket"), total = 15.0),
                Order("3", date = "March 15, 2024", items = listOf("Coat", "Scarf"), total = 30.0)
            )
        )
        ordersAdapter.submitList(activeOrders)

        // Set up onClickListeners
        moreInfoButton.setOnClickListener {
            val selectedOrders = ordersAdapter.getSelectedOrders()
            if (selectedOrders.size == 1) {
                showOrderDetails(selectedOrders.first())
            } else {
                Toast.makeText(this, "Please select exactly one order to view details", Toast.LENGTH_SHORT).show()
            }
        }

        makePaymentButton.setOnClickListener {
            val selectedOrders = ordersAdapter.getSelectedOrders()
            if (selectedOrders.isNotEmpty()) {
                initiatePayment(selectedOrders)
            } else {
                Toast.makeText(this, "Please select at least one order to make a payment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showOrderDetails(order: Order) {
        val orderDetails = """
            Order ID: ${order.id}
            Drop Off Date: ${order.date}
            Items: ${order.items.joinToString(", ")}
            Total Pieces: ${order.items.size}
            Total Cost: $${order.total}
            Status: ${if (order.isReadyForPickup) "Ready for Pickup" else "Not Ready for Pickup"}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Order Details")
            .setMessage(orderDetails)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun initiatePayment(selectedOrders: List<Order>) {
        val totalAmount = selectedOrders.sumOf { it.total * 100 }.toInt()  // Convert to cents
        createPaymentIntent(totalAmount)
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

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
                // Move paid orders to inactive list and mark them as ready for pickup
                val selectedOrders = ordersAdapter.getSelectedOrders()
                selectedOrders.forEach { it.isReadyForPickup = true }
                activeOrders.removeAll(selectedOrders)
                inactiveOrders.addAll(selectedOrders)
                ordersAdapter.clearSelectedOrders()
                ordersAdapter.submitList(activeOrders) // Update active orders list
                inactiveOrdersAdapter.submitList(inactiveOrders) // Update inactive orders list
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
