package com.ogdenscleaners.ogdenscleanersapp.models

data class Order(
    val id: String,    // Order ID
    val date: String,  // Date of the order
    val items: List<String>, // List of items in the order
    val total: Double,  // Total amount for the order
    var isReadyForPickup: Boolean = false // Is the order ready for pickup (default is false)
)
