package com.ogdenscleaners.ogdenscleanersapp.models

data class BillingStatement(
    val statementId: String,
    val customerId: String,
    val date: String,
    val amountOwed: String,
    val orderIds: List<String>,
    val paidStatus: Boolean,
    var isSelected: Boolean
) {
    val totalAmountText: String
        get() = "$$amountOwed"
}
