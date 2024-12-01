package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

    // Initialize adapters with appropriate selection modes
    private lateinit var activeOrdersAdapter: OrdersAdapter
    private lateinit var inactiveOrdersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize PaymentConfiguration with the publishable key
        PaymentConfiguration.init(
            applicationContext,
            getString(R.string.stripe_publishable_key)
        )

        // Initialize Stripe PaymentSheet
        paymentSheet = PaymentSheet(this) { paymentSheetResult ->
            handlePaymentSheetResult(paymentSheetResult)
        }

        // Set up RecyclerViews for active and inactive orders
        setupRecyclerViews()

        // Observers
        orderViewModel.activeOrders.observe(this) { orders ->
            activeOrdersAdapter.submitList(orders)
        }

        orderViewModel.inactiveOrders.observe(this) { orders ->
            inactiveOrdersAdapter.submitList(orders)
        }

        orderViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        orderViewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        orderViewModel.paymentIntentClientSecret.observe(this) { clientSecret ->
            clientSecret?.let { presentPaymentSheet(it) }
        }


        orderViewModel.loadOrders()


        binding.makePaymentButton.setOnClickListener {
            val selectedOrders = activeOrdersAdapter.getSelectedOrders()
            if (selectedOrders.isNotEmpty()) {
                processPaymentForOrders(selectedOrders)
            } else {
                Toast.makeText(this, "Please select at least one order.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupRecyclerViews() {

        activeOrdersAdapter = OrdersAdapter(
            onMoreInfoClick = { order -> showOrderDetails(order) },
            selectionMode = OrdersAdapter.SelectionMode.MULTIPLE
        )
        binding.activeOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.activeOrdersRecyclerView.adapter = activeOrdersAdapter


        inactiveOrdersAdapter = OrdersAdapter(
            onMoreInfoClick = { order -> showOrderDetails(order) },
            selectionMode = OrdersAdapter.SelectionMode.SINGLE
        )
        binding.inactiveOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.inactiveOrdersRecyclerView.adapter = inactiveOrdersAdapter
    }

    private fun showOrderDetails(order: Order) {
        val orderDetails = """
            Order ID: ${order.customerId}
            Drop Off Date: ${order.dropOffDate}
            Items: ${order.items.joinToString(", ") { it.name }}
            Total Pieces: ${order.items.size}
            Total Cost: $${order.orderTotal}
            Status: ${if (order.isReadyForPickup) "Ready for Pickup" else "Not Ready for Pickup"}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Order Details")
            .setMessage(orderDetails)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun presentPaymentSheet(clientSecret: String) {
        val paymentConfiguration = PaymentSheet.Configuration(
            merchantDisplayName = "Ogden's Cleaners"
        )
        paymentSheet.presentWithPaymentIntent(clientSecret, paymentConfiguration)
    }

    private fun handlePaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()

                orderViewModel.loadOrders()
            }
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Payment canceled.", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(
                    this,
                    "Payment failed: ${paymentSheetResult.error.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun processPaymentForOrders(selectedOrders: List<Order>) {
        orderViewModel.initiatePayment(selectedOrders)
    }
}
