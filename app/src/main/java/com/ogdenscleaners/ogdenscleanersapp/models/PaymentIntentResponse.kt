package com.ogdenscleaners.ogdenscleanersapp.models

import com.google.gson.annotations.SerializedName

data class PaymentIntentResponse(
    @SerializedName("client_secret") val clientSecret: String
)
