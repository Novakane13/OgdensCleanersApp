package com.ogdenscleaners.ogdenscleanersapp.models

data class PaymentIntentRequest(
    val amount: Int,
    val currency: String
)
