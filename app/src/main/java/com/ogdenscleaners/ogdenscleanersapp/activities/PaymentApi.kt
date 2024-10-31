package com.ogdenscleaners.ogdenscleanersapp.activities

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApi {
    @POST("create-payment-intent")
    fun createPaymentIntent(@Body paymentData: PaymentData): Call<PaymentIntentResponse>
}

data class PaymentData(val amount: Int, val currency: String)
data class PaymentIntentResponse(val clientSecret: String)
