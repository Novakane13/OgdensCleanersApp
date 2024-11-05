package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityDashboardBinding
import com.ogdenscleaners.ogdenscleanersapp.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        // Setup navigation button click listeners using data binding
        binding.apply {
            navButtonOrders.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, OrdersActivity::class.java))
            }
            navButtonBilling.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, MonthlyBillingActivity::class.java))
            }
            navButtonDelivery.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, DeliveryActivity::class.java))
            }
            navButtonNotifications.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, NotificationsActivity::class.java))
            }
            navButtonFeedback.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, FeedbackActivity::class.java))
            }
            navButtonAccountInfo.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, AccountInfoActivity::class.java))
            }
        }
    }
}
