package com.ogdenscleaners.ogdenscleanersapp.models

import com.google.gson.annotations.SerializedName

data class EphemeralKeyRequest(
    @SerializedName("customer_id") val customerId: String,
    @SerializedName("api_version") val apiVersion: String
)
