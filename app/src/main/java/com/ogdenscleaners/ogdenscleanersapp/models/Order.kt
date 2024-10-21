package com.ogdenscleaners.ogdenscleanersapp.models

data class Order(
    val id: String,    // Order ID
    val status: String, // Status of the order
    val total: Double,  // Total amount for the order
    val items: List<String>, // List of items in the order
    val date: String  // Date of the order
)
