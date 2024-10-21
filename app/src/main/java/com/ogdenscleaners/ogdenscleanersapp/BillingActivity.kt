package com.ogdenscleaners.ogdenscleanersapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ogdenscleaners.ogdenscleanersapp.api.ApiClient
import com.ogdenscleaners.ogdenscleanersapp.api.ApiService
import com.ogdenscleaners.ogdenscleanersapp.models.PaymentIntentRequest
import com.ogdenscleaners.ogdenscleanersapp.ui.theme.OgdensCleanersAppTheme
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.*

class BillingActivity : ComponentActivity() {

    private lateinit var paymentSheet: PaymentSheet
    private var customerConfig: PaymentSheet.CustomerConfiguration? = null
    private var paymentIntentClientSecret: String? = null
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize API Service and PaymentSheet
        apiService = ApiClient.retrofitInstance.create(ApiService::class.java)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        setContent {
            val isPayEnabled = remember { mutableStateOf(false) }
            OgdensCleanersAppTheme {
                BillingScreen(
                    onPayClick = { presentPaymentSheet() },
                    isPayEnabled = isPayEnabled.value
                )

                // Fetch PaymentIntent and Customer information asynchronously
                LaunchedEffect(Unit) {
                    fetchPaymentIntent(isPayEnabled)
                }
            }
        }
    }

    private fun fetchPaymentIntent(isPayEnabled: MutableState<Boolean>) {
        val paymentIntentRequest = PaymentIntentRequest(
            amount = 5000,  // Amount in cents ($50.00)
            currency = "usd"
        )

        // Use coroutine to fetch PaymentIntent
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.createPaymentIntent(paymentIntentRequest).execute() // Synchronous call
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        paymentIntentClientSecret = data.clientSecret
                        customerConfig = PaymentSheet.CustomerConfiguration(
                            id = data.customerId,
                            ephemeralKeySecret = data.ephemeralKey
                        )
                        withContext(Dispatchers.Main) {
                            isPayEnabled.value = true
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast(this@BillingActivity, "Failed to fetch PaymentIntent")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast(this@BillingActivity, "Error: ${e.message}")
                }
            }
        }
    }

    // Present the Payment Sheet
    private fun presentPaymentSheet() {
        val configuration = PaymentSheet.Configuration(
            merchantDisplayName = "Ogden's Cleaners",
            customer = customerConfig,
            allowsDelayedPaymentMethods = true
        )

        paymentIntentClientSecret?.let {
            paymentSheet.presentWithPaymentIntent(it, configuration)
        } ?: run {
            showToast(this, "PaymentIntent is null, please try again later.")
        }
    }

    // Handle the result of the Payment Sheet
    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Completed -> showToast(this, "Payment complete!")
            is PaymentSheetResult.Canceled -> showToast(this, "Payment canceled!")
            is PaymentSheetResult.Failed -> showToast(this, "Payment failed: ${paymentResult.error.localizedMessage}")
        }
    }

    // Helper function to display Toast messages
    private fun showToast(context: ComponentActivity, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingScreen(onPayClick: () -> Unit, isPayEnabled: Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Billing") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = "Amount: $50.00")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onPayClick,
                enabled = isPayEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pay Now")
            }
        }
    }
}
