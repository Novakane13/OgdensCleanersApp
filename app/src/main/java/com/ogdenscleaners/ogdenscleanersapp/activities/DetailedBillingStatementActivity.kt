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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        observeViewModel()
        fetchFilteredOrders()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

private fun setupRecyclerView() {
    adapter = OrdersAdapter(
        onMoreInfoClick = { order -> showOrderDetailsDialog(order) },
        selectionMode = OrdersAdapter.SelectionMode.SINGLE
    )
    binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
    binding.recyclerViewOrders.adapter = adapter
}

    private fun moreinfobutton() {
        binding.moreinfobutton.setOnClickListener {
            val selectedOrder = adapter.getSelectedOrder()
            if (selectedOrder != null) {
                showOrderDetailsDialog(selectedOrder)
            } else {
                Toast.makeText(this, "Please select an order.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchFilteredOrders() {
        val orderIds = intent.getStringArrayListExtra("order_ids") ?: emptyList()
        val mockOrders = createMockOrders()
        val filteredOrders = mockOrders.filter { it.id in orderIds }

        adapter.submitList(filteredOrders)
    }

    private fun observeViewModel() {
        billingViewModel.orders.observe(this) { result ->
            result?.onSuccess { orders ->
                adapter.submitList(orders)
            }?.onFailure { error ->
                Toast.makeText(this, "Failed to load orders: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }



    private fun showOrderDetailsDialog(order: Order) {
        val details = order.items.joinToString("\n") {
            "Item: ${it.name}, Color: ${it.color}, Pattern: ${it.pattern}, Cost: $${"%.2f".format(it.price)}"
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Order Details")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun createMockOrders(): List<Order> {
        // Mock data for testing purposes
        val mockOrders = listOf(
            Order(
                id = "ORD001",
                customerId = "CUST001",
                dropOffDate = "2024-11-20",
                numOfPieces = 3,
                orderTotal = 18.0,
                items = listOf(
                    ClothingItem(
                        name = "Shirt",
                        quantity = 4,
                        color = "Blue",
                        pattern = "Striped",
                        upcharges = "Linen",
                        price = 5.0
                    ),
                    ClothingItem(
                        name = "Pants",
                        quantity = 1,
                        color = "Black",
                        pattern = "Solid",
                        upcharges = "None",
                        price = 8.0
                    )
                )
            ),
            Order(
                id = "ORD002",
                customerId = "CUST002",
                dropOffDate = "2024-11-21",
                numOfPieces = 2,
                orderTotal = 27.0,
                items = listOf(
                    ClothingItem(
                        name = "Coat",
                        quantity = 2,
                        color = "Brown",
                        pattern = "Checkered",
                        upcharges = "Winter",
                        price = 15.0
                    ),
                    ClothingItem(
                        name = "Sweater",
                        quantity = 3,
                        color = "Red",
                        pattern = "Knitted",
                        upcharges = "Wool",
                        price = 12.0
                    )
                )
            )
        )

        billingViewModel.loadMockOrders(mockOrders)
        return mockOrders
    }
}

