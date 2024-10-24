package com.ogdenscleaners.ogdenscleanersapp.models

data class BillingStatement(
    val id: String,                 // Unique ID for the statement (could be UUID)
    val month: String,              // Month of the billing statement (e.g., "January 2024")
    val totalAmount: String,
    val totalAmountText: String,          // Total amount for the month (e.g., "$100.00")
    val details: String,            // Statement details or any notes
    val paidStatus: Boolean         // Indicates whether the bill is paid or not
)
