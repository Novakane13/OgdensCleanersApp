package com.ogdenscleaners.ogdenscleanersapp.services

import android.content.Context
import com.stripe.android.Stripe
import com.stripe.android.ApiResultCallback
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.PaymentMethodCreateParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PaymentHelper(context: Context, publishableKey: String) {

    private val stripe: Stripe = Stripe(context, publishableKey)

    /**
     * Creates a PaymentMethod for the given card parameters.
     * This is a suspend function that performs the operation asynchronously.
     */
    suspend fun createPaymentMethod(paymentMethodParams: PaymentMethodCreateParams): Result<PaymentMethod> {
        return withContext(Dispatchers.IO) {
            try {
                val paymentMethod = suspendCancellableCoroutine<PaymentMethod> { continuation ->
                    stripe.createPaymentMethod(
                        paymentMethodCreateParams = paymentMethodParams,
                        idempotencyKey = null, // Provide idempotency key if needed
                        callback = object : ApiResultCallback<PaymentMethod> {
                            override fun onSuccess(result: PaymentMethod) {
                                continuation.resume(result)
                            }

                            override fun onError(e: Exception) {
                                continuation.resumeWithException(e)
                            }
                        }
                    )
                }
                Result.success(paymentMethod)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Sends a token (or PaymentMethod ID) to the server for further processing.
     */
    fun sendTokenToServer(token: String) {
        val url = "http://10.0.2.2:4242/" // Localhost for emulator
        val json = """
            {
                "token": "$token"
            }
        """.trimIndent()

        val client = OkHttpClient()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to send token to server: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Successfully sent token to server")
                } else {
                    println("Failed to send token to server: ${response.message}")
                }
            }
        })
    }
}
