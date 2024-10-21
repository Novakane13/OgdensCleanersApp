package com.ogdenscleaners.ogdenscleanersapp.models

data class DeliveryRequest(
    val userId: String,
    val address: String,
    val date: String,
    val time: String,
    val instructions: String
)
