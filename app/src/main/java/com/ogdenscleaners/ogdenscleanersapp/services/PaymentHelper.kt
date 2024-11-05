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
                    callback(Result.success(token))
                }

                override fun onError(e: Exception) {
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
