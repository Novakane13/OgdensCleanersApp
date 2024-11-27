package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityDashboardBinding
import com.ogdenscleaners.ogdenscleanersapp.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize View Binding
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationButtons()
    }

    private fun setupNavigationButtons() {
        binding.apply {
            navButtonOrders.setOnClickListener {
                navigateToActivity(OrdersActivity::class.java)
            }
            navButtonBilling.setOnClickListener {
                navigateToActivity(MonthlyBillingActivity::class.java)
            }
            navButtonDelivery.setOnClickListener {
                navigateToActivity(DeliveryActivity::class.java)
            }
            navButtonNotifications.setOnClickListener {
                navigateToActivity(NotificationsActivity::class.java)
            }
            navButtonFeedback.setOnClickListener {
                navigateToActivity(FeedbackActivity::class.java)
            }
            navButtonAccountInfo.setOnClickListener {
                navigateToActivity(AccountInfoActivity::class.java)
            }
        }
    }

    private fun navigateToActivity(targetActivity: Class<*>) {
        startActivity(Intent(this, targetActivity))
    }
}
