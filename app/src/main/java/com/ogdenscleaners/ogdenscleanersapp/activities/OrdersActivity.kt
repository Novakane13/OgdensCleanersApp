package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R

class OrdersActivity : AppCompatActivity() {

    private lateinit var activeOrdersRecyclerView: RecyclerView
    private lateinit var inactiveOrdersRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        // Initialize views
        activeOrdersRecyclerView = findViewById(R.id.active_orders_recycler_view)
        inactiveOrdersRecyclerView = findViewById(R.id.inactive_orders_recycler_view)

        // Setup RecyclerView for active orders
        activeOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        // TODO: Set your adapter for active orders here
        // activeOrdersRecyclerView.adapter = yourAdapter

        // Setup RecyclerView for inactive orders
        inactiveOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        // TODO: Set your adapter for inactive orders here
        // inactiveOrdersRecyclerView.adapter = yourAdapter

        // Setup buttons for more info, add to payment, and make payment
        val moreInfoButton = findViewById<Button>(R.id.moreInfoButton)
        val addToPaymentButton = findViewById<Button>(R.id.addToPaymentButton)
        val makePaymentButton = findViewById<Button>(R.id.makePaymentButton)

        moreInfoButton.setOnClickListener {
            // Handle "More Info" button click
        }

        addToPaymentButton.setOnClickListener {
            // Handle "Add to Payment" button click
        }

        makePaymentButton.setOnClickListener {
            // Handle "Make Payment" button click
        }
    }
}
