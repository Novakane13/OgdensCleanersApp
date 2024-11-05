// DeliveryRepository.kt
package com.ogdenscleaners.ogdenscleanersapp.repository

import android.content.SharedPreferences
import com.ogdenscleaners.ogdenscleanersapp.models.PickupRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeliveryRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun saveServiceStopDate(date: String) {
        sharedPreferences.edit().apply {
            putString("stop_service_date", date)
            apply()
        }
    }

    fun savePickupRequest(pickupRequest: PickupRequest) {
        sharedPreferences.edit().apply {
            putString("pickup_date", pickupRequest.date)
            putString("pickup_note", pickupRequest.note)
            apply()
        }
    }

    fun tempStopService() {
        // Logic to save a temporary stop service status, if needed
    }
}
