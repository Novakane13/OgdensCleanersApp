package com.ogdenscleaners.ogdenscleanersapp.models

import com.ogdenscleaners.ogdenscleanersapp.models.DeliveryResponse

data class DeliveryResponse(
    val success: Boolean,
    val message: String
) {
    fun enqueue(callback: retrofit2.Callback<DeliveryResponse>) {}

}
