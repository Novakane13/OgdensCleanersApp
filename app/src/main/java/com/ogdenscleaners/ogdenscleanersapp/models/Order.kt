package com.ogdenscleaners.ogdenscleanersapp.models

data class Order(
    val id: String,
    val customerId: String,
    val dropOffDate: String,
    val numOfPieces: Int,
    val orderTotal: Double,
    val items: List<ClothingItem>,
    val isReadyForPickup: Boolean = false,
    val pickedup: Boolean = false,
    val paidfor: Boolean = false
)

    data class ClothingItem(
        val name: String,
        val quantity: Int,
        val details: String,
        val price: Double
    )
