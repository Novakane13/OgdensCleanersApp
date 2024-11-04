package com.ogdenscleaners.ogdenscleanersapp.models

data class Customer(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var creditCards: MutableList<CreditCard> = mutableListOf()
) {
    data class CreditCard(
        var cardholderName: String? = null,
        var lastFourDigits: String? = null,
        var expirationDate: String? = null,
        var cardToken: String? = null
    )
}