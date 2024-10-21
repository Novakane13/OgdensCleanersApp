package com.ogdenscleaners.ogdenscleanersapp.models

data class PaymentRequest(
    val amount: Int, // Amount in cents (e.g., 5000 for $50.00)
    val currency: String, // e.g., "usd"
    val paymentMethodId: String // Payment method identifier
)
