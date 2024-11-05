package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.adapters.OrdersAdapter
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityOrdersBinding
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import com.ogdenscleaners.ogdenscleanersapp.viewmodel.OrderViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private lateinit var paymentSheet: PaymentSheet
    private val orderViewModel: OrderViewModel by viewModels()
    private val activeOrdersAdapter = OrdersAdapter(onMoreInfoClick = { order -> showOrderDetails(order) })
    private val inactiveOrdersAdapter = OrdersAdapter(onMoreInfoClick = { order -> showOrderDetails(order) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize PaymentConfiguration with the test key
        PaymentConfiguration.init(applicationContext, "pk_test_51QEmC6F9q8Y1A3UES8uzimDczaKS3xMRUNr9QN4vhQN8wjktGMEONNrWWP7mFCJRrdYDmTPADDDVxn1GvS0mTkCw00XlEDwkSY")

        // Initialize Stripe PaymentSheet
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        // Set up RecyclerViews
        binding.activeOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.activeOrdersRecyclerView.adapter = activeOrdersAdapter
        binding.inactiveOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.inactiveOrdersRecyclerView.adapter = inactiveOrdersAdapter

        // Observers
        orderViewModel.activeOrders.observe(this, Observer { orders ->
            activeOrdersAdapter.submitList(orders)
        })

        orderViewModel.inactiveOrders.observe(this, Observer { orders ->
            inactiveOrdersAdapter.submitList(orders)
        })

        orderViewModel.loading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        orderViewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        orderViewModel.paymentIntentClientSecret.observe(this, Observer { clientSecret ->
            clientSecret?.let { presentPaymentSheet(it) }
        })

        // Load orders
        orderViewModel.loadOrders()

        // Set up onClickListeners
        binding.moreInfoButton.setOnClickListener {
            val selectedOrders = activeOrdersAdapter.getSelectedOrders()
            if (selectedOrders.size == 1) {
                showOrderDetails(selectedOrders.first())
            } else {
                Toast.makeText(this, "Please select exactly one order to view details", Toast.LENGTH_SHORT).show()
            }
        }

        binding.makePaymentButton.setOnClickListener {
            val selectedOrders = activeOrdersAdapter.getSelectedOrders()
            if (selectedOrders.isNotEmpty()) {
                orderViewModel.initiatePayment(selectedOrders)
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

    private fun presentPaymentSheet(clientSecret: String) {
        paymentSheet.presentWithPaymentIntent(
            clientSecret,
            PaymentSheet.Configuration(merchantDisplayName = "Ogden's Cleaners")
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
                val selectedOrders = activeOrdersAdapter.getSelectedOrders()
                orderViewModel.completePayment(selectedOrders)
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
