package com.ogdenscleaners.ogdenscleanersapp.models

data class PaymentResponse(
    val success: Boolean,
    val transactionId: String,
    val message: String? // Optional message (e.g., in case of failure)
)
