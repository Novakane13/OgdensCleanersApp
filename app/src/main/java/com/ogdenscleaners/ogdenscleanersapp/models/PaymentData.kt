package com.ogdenscleaners.ogdenscleanersapp.models

data class PaymentData(
    val customerId: String,
    val items: List<ClothingItem>,
    val fullamount: Int,
    val currency: String
)
