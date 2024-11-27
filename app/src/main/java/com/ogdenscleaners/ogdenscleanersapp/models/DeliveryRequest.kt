package com.ogdenscleaners.ogdenscleanersapp.models

data class DeliveryRequest(
    val customerId: String,
    val address: String,
    val date: String,
    val instructions: String,
    val needsPickup: Boolean = false,
    val serviceStopped: Boolean = false
)
