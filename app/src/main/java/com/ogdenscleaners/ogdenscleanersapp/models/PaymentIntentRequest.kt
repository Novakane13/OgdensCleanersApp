package com.ogdenscleaners.ogdenscleanersapp.models

import com.google.gson.annotations.SerializedName

data class PaymentIntentRequest(
    @SerializedName("amount") val amount: Int,
    @SerializedName("currency") val currency: String = "usd",
    @SerializedName("customer_id") val customerId: String,
    @SerializedName("payment_method_id") val paymentMethodId: String
)
