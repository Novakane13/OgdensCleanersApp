package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ogdenscleaners.ogdenscleanersapp.adapters.OrdersAdapter
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityDetailedBillingStatementBinding
import com.ogdenscleaners.ogdenscleanersapp.models.ClothingItem
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import com.ogdenscleaners.ogdenscleanersapp.viewmodel.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedBillingStatementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedBillingStatementBinding
    private val billingViewModel: BillingViewModel by viewModels()
    private lateinit var adapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBillingStatementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup RecyclerView for displaying orders
        setupRecyclerView()

        // Observe ViewModel for data changes
        observeViewModel()

        // Fetch orders for the selected billing statement
        fetchMockDataForTesting() // Replace with API call for production
    }
// Handle back button in the toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupRecyclerView() {
        adapter = OrdersAdapter { order ->
            showOrderDetailsDialog(order) // Trigger dialog for order details
        }
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOrders.adapter = adapter
    }

    private fun observeViewModel() {
        billingViewModel.orders.observe(this) { result ->
            result.onSuccess { orders ->
                if (orders.isEmpty()) {
                    binding.noDataText.visibility = View.VISIBLE
                } else {
                    binding.noDataText.visibility = View.GONE
                }
                adapter.submitList(orders)
            }.onFailure { error ->
                Toast.makeText(
                    this,
                    "Failed to load orders: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showOrderDetailsDialog(order: Order) {
        val itemsDetails = order.items.joinToString(separator = "\n") { item ->
            "${item.name} - ${item.quantity} pcs @ $${item.price} each"
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Order Details")
            .setMessage(
                """
            Order ID: ${order.id}
            Drop Off Date: ${order.dropOffDate}
            
            Items:
            $itemsDetails
            """.trimIndent()
            )
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun fetchMockDataForTesting() {
        // Mock data for testing purposes
        val mockOrders = listOf(
            Order(
                id = "ORD001",
                customerId = "CUST001",
                dropOffDate = "2024-11-20",
                numOfPieces = 3,
                orderTotal = 18.0,
                items = listOf(
                    ClothingItem(name = "Shirt", quantity = 4, details = "Size M", price = 5.0),
                    ClothingItem(name = "Pants", quantity = 1, details = "Size L", price = 8.0)
                )
            ),
            Order(
                id = "ORD002",
                customerId = "CUST002",
                dropOffDate = "2024-11-21",
                numOfPieces = 2,
                orderTotal = 27.0,
                items = listOf(
                    ClothingItem(name = "Coat", quantity = 2, details = "Winter coat", price = 15.0),
                    ClothingItem(name = "Sweater", quantity = 3, details = "Wool sweater", price = 12.0)
                )
            )
        )

        billingViewModel.setMockOrders(mockOrders)
    }
}
