package com.ogdenscleaners.ogdenscleanersapp.models

data class PaymentIntentResponse(
    val clientSecret: String
) {
    val isSuccessful: Boolean
        get() = clientSecret.isNotEmpty()
}
