package com.ogdenscleaners.ogdenscleanersapp.services

import android.content.Context
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.ogdenscleaners.ogdenscleanersapp.api.ApiClient
import com.ogdenscleaners.ogdenscleanersapp.api.ApiService
import com.ogdenscleaners.ogdenscleanersapp.api.PaymentIntentRequest
import com.ogdenscleaners.ogdenscleanersapp.api.PaymentIntentResponse
import com.ogdenscleaners.ogdenscleanersapp.models.EphemeralKeyRequest
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
    private val stripePublishableKey: String = "pk_test_51QEmC6F9q8Y1A3UES8uzimDczaKS3xMRUNr9QN4vhQN8wjktGMEONNrWWP7mFCJRrdYDmTPADDDVxn1GvS0mTkCw00XlEDwkSY"

    private val apiService: ApiService = ApiClient
        .getRetrofitInstance("http://10.0.2.2:4242/")
        .create(ApiService::class.java)

    private val paymentSheet: PaymentSheet = PaymentSheet(context as androidx.appcompat.app.AppCompatActivity, ::handlePaymentResult)

    init {
        // Initialize the Stripe PaymentConfiguration with the live key
        PaymentConfiguration.init(context, stripePublishableKey)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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
                )

                withContext(Dispatchers.Main) {
                    response?.let {
                        onSuccess(it.id)
                    } ?: onFailure("Ephemeral key creation failed")
                }
            } catch (e: HttpException) {
                logAndNotifyFailure("Failed to create ephemeral key", e, onFailure)
            } catch (e: Exception) {
                logAndNotifyFailure("Unexpected error occurred", e, onFailure)

            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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

                val response = apiService.createPaymentIntent(paymentIntentRequest)

                withContext(Dispatchers.Main) {
                    response?.let {
                        onSuccess(it.clientSecret)
                    } ?: onFailure("Payment intent creation failed")
                }
            } catch (e: HttpException) {
                logAndNotifyFailure("Failed to create payment intent", e, onFailure)
            } catch (e: Exception) {
                logAndNotifyFailure("Unexpected error occurred", e, onFailure)
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

    private fun logAndNotifyFailure(message: String, exception: Exception, onFailure: (String) -> Unit) {
        Log.e("PaymentManager", "$message: ${exception.message}", exception)
        CoroutineScope(Dispatchers.Main).launch {
            onFailure("$message. Please try again later.")
        }
    }
}
