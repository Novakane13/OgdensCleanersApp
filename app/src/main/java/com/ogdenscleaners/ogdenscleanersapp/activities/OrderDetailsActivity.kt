package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import com.ogdenscleaners.ogdenscleanersapp.models.ClothingItem

class OrderDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        val orderId = intent.getStringExtra("order_id") ?: ""
        loadOrderDetails(orderId)
    }

    private fun loadOrderDetails(orderId: String) {
        // Mock data for demonstration
        val mockGarments = listOf(
            ClothingItem("Shirt", 3,"White", "plaid", "linen", 4.99),
            ClothingItem("Pants", 2,"Blue", "striped", "silk, heavy", 9.99)
        )

        // Bind the mock data to a RecyclerView or UI component
    }
}
