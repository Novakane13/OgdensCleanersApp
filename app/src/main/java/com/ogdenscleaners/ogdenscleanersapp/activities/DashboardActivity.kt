package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.activities.OrdersActivity
import com.ogdenscleaners.ogdenscleanersapp.activities.MonthlyBillingActivity
import com.ogdenscleaners.ogdenscleanersapp.activities.DeliveryActivity
import com.ogdenscleaners.ogdenscleanersapp.activities.NotificationsActivity
import com.ogdenscleaners.ogdenscleanersapp.activities.FeedbackActivity
import com.ogdenscleaners.ogdenscleanersapp.activities.AccountInfoActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Set up navigation button click listeners using findViewById
        val navButtonOrders: Button = findViewById(R.id.nav_button_orders)
        val navButtonBilling: Button = findViewById(R.id.nav_button_billing)
        val navButtonDelivery: Button = findViewById(R.id.nav_button_delivery)
        val navButtonNotifications: Button = findViewById(R.id.nav_button_notifications)
        val navButtonFeedback: Button = findViewById(R.id.nav_button_feedback)
        val navButtonAccountInfo: Button = findViewById(R.id.nav_button_account_info)

        navButtonOrders.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }

        navButtonBilling.setOnClickListener {
            startActivity(Intent(this, MonthlyBillingActivity::class.java))
        }

        navButtonDelivery.setOnClickListener {
            startActivity(Intent(this, DeliveryActivity::class.java))
        }

        navButtonNotifications.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        navButtonFeedback.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }

        navButtonAccountInfo.setOnClickListener {
            startActivity(Intent(this, AccountInfoActivity::class.java))
        }
    }
}
