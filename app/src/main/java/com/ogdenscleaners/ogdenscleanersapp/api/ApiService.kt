package com.ogdenscleaners.ogdenscleanersapp.api

import com.google.gson.annotations.SerializedName
import com.ogdenscleaners.ogdenscleanersapp.models.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class PaymentIntentRequest(
    val amount: Int,
    val currency: String,
    @SerializedName("customer_id") val customerId: String,
    @SerializedName("payment_method_id") val paymentMethodId: String
)

data class PaymentIntentResponse(
    @SerializedName("client_secret") val clientSecret: String
)


interface ApiService {
    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") userId: String): Account

    @PUT("accounts/{id}")
    suspend fun updateAccount(@Path("id") userId: String, @Body account: Account)

    @GET("accounts/{id}/orders")
    suspend fun getOrders(@Path("id") userId: String): List<Order>

    @POST("deliveries")
    suspend fun requestDelivery(@Body deliveryRequest: DeliveryRequest): DeliveryResponse

    @POST("payments/monthly")
    suspend fun makeMonthlyBillingPayment(@Body paymentRequest: PaymentIntentRequest): PaymentIntentResponse

    @POST("payments/orders")
    suspend fun makeOrderPayment(@Body paymentRequest: PaymentIntentRequest): PaymentIntentResponse

    @POST("/ephemeral_keys")
    suspend fun createEphemeralKey(@Body ephemeralKeyRequest: EphemeralKeyRequest): EphemeralKeyResponse

    @POST("/create-payment-intent")
    suspend fun createPaymentIntent(@Body request: PaymentIntentRequest): PaymentIntentResponse
}
