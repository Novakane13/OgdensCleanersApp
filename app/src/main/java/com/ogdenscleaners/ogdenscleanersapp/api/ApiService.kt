package com.ogdenscleaners.ogdenscleanersapp.api

import com.ogdenscleaners.ogdenscleanersapp.models.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") userId: String): Account

    @PUT("accounts/{id}")
    suspend fun updateAccount(@Path("id") userId: String, @Body account: Account)

    @GET("accounts/{id}/orders")
    suspend fun getOrders(@Path("id") userId: String): List<Order>

    @POST("payments")
    suspend fun makePayment(@Body paymentRequest: PaymentRequest): PaymentResponse

    @POST("deliveries")
    suspend fun requestDelivery(@Body deliveryRequest: DeliveryRequest): DeliveryResponse

    @POST("/ephemeral_keys")
    fun createEphemeralKey(@Body ephemeralKeyRequest: EphemeralKeyRequest): Call<EphemeralKeyResponse>

    @POST("/create-payment-intent")
    fun createPaymentIntent(@Body paymentIntentRequest: JSONObject): Call<PaymentIntentResponse>
}
