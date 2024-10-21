package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.adapters.BillingStatementAdapter

class MonthlyBillingActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var billingRecyclerView: RecyclerView
    private lateinit var adapter: BillingStatementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_billing)

        // Setup navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout)
        val logoButton: ImageButton = findViewById(R.id.logoButton)
        logoButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Setup the RecyclerView for billing statements
        billingRecyclerView = findViewById(R.id.billingStatementsRecyclerView)
        billingRecyclerView.layoutManager = LinearLayoutManager(this)

        // Sample Data
        val statements = listOf(
            BillingStatement("January 2024", "$100.00"),
            BillingStatement("February 2024", "$120.00")
        )

        adapter = BillingStatementAdapter(statements, onPayClickListener = { statement ->
            Toast.makeText(this, "Processing payment for ${statement.month}", Toast.LENGTH_SHORT).show()
            // Integrate Stripe payment here
        }, onDetailsClickListener = { statement ->
            Toast.makeText(this, "Viewing details for ${statement.month}", Toast.LENGTH_SHORT).show()
            // Show detailed statement here
        })

        billingRecyclerView.adapter = adapter
    }
}

data class BillingStatement(val month: String, val totalAmount: String)
