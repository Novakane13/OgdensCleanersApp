package com.ogdenscleaners.ogdenscleanersapp.services

import android.content.Context
import com.stripe.android.Stripe
import com.stripe.android.ApiResultCallback
import com.stripe.android.model.CardParams
import com.stripe.android.model.Token
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

class PaymentHelper(context: Context, publishableKey: String) {

    private val stripe: Stripe = Stripe(context, publishableKey)

    // Function to tokenize the card
    fun tokenizeCard(cardParams: CardParams, callback: (Result<Token>) -> Unit) {
        stripe.createCardToken(
            cardParams,
            object : ApiResultCallback<Token> {
                override fun onSuccess(token: Token) {
                    // Invoke the callback with success
                    callback(Result.success(token))
                }

                override fun onError(e: Exception) {
                    // Invoke the callback with failure
                    callback(Result.failure(e))
                }
            }
        )
    }


    private fun sendTokenToServer(token: String) {
        val url = "https://your-backend-server-url.com/charge"
        val json = """
        {
            "token": "$token"
        }
        """.trimIndent()

        // Use an HTTP library to make the request (like OkHttp)
        val client = OkHttpClient()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle the error, e.g., show a message to the user
                println("Failed to send token to server: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // Handle success, e.g., show a success message
                    println("Successfully sent token to server")
                } else {
                    // Handle failure, e.g., show a failure message
                    println("Failed to send token to server: ${response.message}")
                }
            }
        })
    }
}
