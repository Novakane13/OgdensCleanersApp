package com.ogdenscleaners.ogdenscleanersapp.models

import kotlinx.serialization.Serializable

@Serializable
data class AppCustomer(
    var customerId: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var email: String? = null,
    var notes: String? = null,
    var creditCards: MutableList<CreditCard> = mutableListOf()
) {
    @Serializable
    data class CreditCard(
        var cardholderName: String? = null,
        var lastFourDigits: String? = null,
        var expirationDate: String? = null,
        var cardToken: String? = null
    )
}