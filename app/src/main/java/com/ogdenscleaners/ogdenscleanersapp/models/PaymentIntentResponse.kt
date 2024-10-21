package com.ogdenscleaners.ogdenscleanersapp.models

data class PaymentIntentResponse(
    val clientSecret: String,
    val customerId: String,
    val ephemeralKey: String
) {
    val isSuccessful: Boolean
        get() = clientSecret.isNotEmpty() && customerId.isNotEmpty() && ephemeralKey.isNotEmpty()
}

// Separate function or method elsewhere
fun enqueue(callback: retrofit2.Callback<PaymentIntentResponse>) {
    // Your enqueue logic goes here
}
