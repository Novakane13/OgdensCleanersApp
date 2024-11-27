package com.ogdenscleaners.ogdenscleanersapp.services

import android.content.Context
import android.util.Log
import com.ogdenscleaners.ogdenscleanersapp.api.ApiClient
import com.ogdenscleaners.ogdenscleanersapp.api.ApiService
import com.ogdenscleaners.ogdenscleanersapp.models.EphemeralKeyRequest
import com.ogdenscleaners.ogdenscleanersapp.models.PaymentIntentRequest
import com.ogdenscleaners.ogdenscleanersapp.models.PaymentIntentResponse
import com.google.gson.Gson
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentManager(
    private val context: Context,
    private val paymentSheetResultHandler: (PaymentSheetResult) -> Unit
) {

    // Replace with your Stripe live key (ensure it is secure)
    private val stripePublishableKey: String = "pk_live_your_live_key_here"

    private val apiService: ApiService = ApiClient
        .getRetrofitInstance("http://10.0.2.2:4242/")
        .create(ApiService::class.java)

    private val paymentSheet: PaymentSheet = PaymentSheet(context as androidx.appcompat.app.AppCompatActivity, ::handlePaymentResult)

    init {
        // Initialize the Stripe PaymentConfiguration with the live key
        PaymentConfiguration.init(context, stripePublishableKey)
    }

    suspend fun createEphemeralKey(
        customerId: String,
        apiVersion: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.createEphemeralKey(
                    EphemeralKeyRequest(customerId, apiVersion)
                ).execute()

                withContext(Dispatchers.Main) {
                    processResponse(
                        response,
                        { onSuccess(it.id) },
                        { onFailure("Failed to parse ephemeral key response") }
                    )
                }
            } catch (e: Exception) {
                logAndNotifyFailure("Failed to create ephemeral key", e, onFailure)
            }
        }
    }

    suspend fun createPaymentIntent(
        paymentMethodId: String,
        customerId: String,
        amount: Int,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val paymentIntentRequest = PaymentIntentRequest(
                    amount = amount,
                    currency = "usd",
                    customerId = customerId,
                    paymentMethodId = paymentMethodId
                )

                val response = apiService.createPaymentIntent(paymentIntentRequest).execute()

                withContext(Dispatchers.Main) {
                    processResponse(
                        response,
                        { onSuccess(it.clientSecret) },
                        { onFailure("Failed to parse payment intent response") }
                    )
                }
            } catch (e: Exception) {
                logAndNotifyFailure("Failed to create payment intent", e, onFailure)
            }
        }
    }

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

    private fun handlePaymentResult(paymentSheetResult: PaymentSheetResult) {
        paymentSheetResultHandler(paymentSheetResult)
    }

    private inline fun <T> processResponse(
        response: retrofit2.Response<T>,
        onSuccess: (T) -> Unit,
        onFailure: () -> Unit
    ) {
        if (response.isSuccessful) {
            response.body()?.let {
                onSuccess(it)
            } ?: onFailure()
        } else {
            onFailure()
            Log.e("PaymentManager", "Server Error: ${response.message()}")
        }
    }

    private fun logAndNotifyFailure(message: String, exception: Exception, onFailure: (String) -> Unit) {
        Log.e("PaymentManager", "$message: ${exception.message}", exception)
        CoroutineScope(Dispatchers.Main).launch {
            onFailure("$message. Please try again later.")
        }
    }
}
