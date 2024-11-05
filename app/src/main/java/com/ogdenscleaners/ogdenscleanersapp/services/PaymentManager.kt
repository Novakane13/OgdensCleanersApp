package com.ogdenscleaners.ogdenscleanersapp.services

import android.content.Context
import android.util.Log
import com.ogdenscleaners.ogdenscleanersapp.api.ApiClient
import com.ogdenscleaners.ogdenscleanersapp.api.ApiService
import com.ogdenscleaners.ogdenscleanersapp.models.EphemeralKeyRequest
import com.ogdenscleaners.ogdenscleanersapp.models.PaymentIntentRequest
import com.ogdenscleaners.ogdenscleanersapp.models.PaymentIntentResponse
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class PaymentManager(context: Context, private val paymentSheetResultHandler: (PaymentSheetResult) -> Unit) {

    private val apiService: ApiService = ApiClient
        .getRetrofitInstance("http://10.0.2.2:4242/")
        .create(ApiService::class.java)
    private val paymentSheet: PaymentSheet = PaymentSheet(context as androidx.appcompat.app.AppCompatActivity, ::handlePaymentResult)

    init {
        PaymentConfiguration.init(context, context.getString(R.string.stripe_publishable_key))
    }

    // Create an ephemeral key using coroutines for cleaner async handling
    fun createEphemeralKey(customerId: String, apiVersion: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.createEphemeralKey(EphemeralKeyRequest(customerId, apiVersion)).execute()
                withContext(Dispatchers.Main) {
                    handleEphemeralKeyResponse(response, onSuccess, onFailure)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure("Failed to create ephemeral key. Please try again later.")
                    Log.e("PaymentManager", "Exception during ephemeral key creation: ${e.message}", e)
                }
            }
        }
    }

    // Create a payment intent using coroutines for cleaner async handling
    fun createPaymentIntent(customerId: String, amount: Int, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.createPaymentIntent(PaymentIntentRequest(amount, "usd", customerId)).execute()
                withContext(Dispatchers.Main) {
                    handlePaymentIntentResponse(response, onSuccess, onFailure)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure("Failed to create payment intent. Please try again later.")
                    Log.e("PaymentManager", "Exception during payment intent creation: ${e.message}", e)
                }
            }
        }
    }

    // Present the PaymentSheet to the user
    fun presentPaymentSheet(clientSecret: String, ephemeralKey: String, customerId: String) {
        val paymentConfiguration = PaymentSheet.Configuration(
            merchantDisplayName = "Ogden's Cleaners",
            customer = PaymentSheet.CustomerConfiguration(
                id = customerId,
                ephemeralKeySecret = ephemeralKey
            )
        )
        paymentSheet.presentWithPaymentIntent(clientSecret, paymentConfiguration)
    }

    // Handle PaymentSheet result
    private fun handlePaymentResult(paymentSheetResult: PaymentSheetResult) {
        paymentSheetResultHandler(paymentSheetResult)
    }

    // Handle ephemeral key response to centralize response handling
    private fun handleEphemeralKeyResponse(
        response: Response<EphemeralKeyResponse>,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (response.isSuccessful) {
            response.body()?.let {
                onSuccess(it.id)
            } ?: onFailure("Failed to parse ephemeral key response")
        } else {
            onFailure("Server Error: ${response.message()}")
            Log.e("PaymentManager", "Failed to create ephemeral key - Server Error: ${response.message()}")
        }
    }

    // Handle payment intent response to centralize response handling
    private fun handlePaymentIntentResponse(
        response: Response<PaymentIntentResponse>,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (response.isSuccessful) {
            response.body()?.let {
                onSuccess(it.clientSecret)
            } ?: onFailure("Failed to parse payment intent response")
        } else {
            onFailure("Server Error: ${response.message()}")
            Log.e("PaymentManager", "Failed to create payment intent - Server Error: ${response.message()}")
        }
    }
}

