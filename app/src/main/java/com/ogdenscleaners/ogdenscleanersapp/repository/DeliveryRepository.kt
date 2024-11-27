package com.ogdenscleaners.ogdenscleanersapp.repository

import android.content.SharedPreferences
import com.ogdenscleaners.ogdenscleanersapp.models.DeliveryRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeliveryRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_PICKUP_DATE = "pickup_date"
        private const val KEY_PICKUP_INSTRUCTIONS = "pickup_instructions"
        private const val KEY_STOP_SERVICE_DATE = "stop_service_date"
    }

    /**
     * Saves a delivery pickup request to SharedPreferences.
     * @param request DeliveryRequest containing pickup details.
     */
    suspend fun savePickupRequest(request: DeliveryRequest) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit().apply {
                    putString(KEY_PICKUP_DATE, request.date)
                    putString(KEY_PICKUP_INSTRUCTIONS, request.instructions)
                    apply()
                }
            } catch (e: Exception) {
                // Log or handle the error (consider using Timber or similar logging library)
                throw Exception("Failed to save pickup request: ${e.message}")
            }
        }
    }

    /**
     * Retrieves the last saved delivery pickup request from SharedPreferences.
     * @return DeliveryRequest if data exists, null otherwise.
     */
    suspend fun getLastPickupRequest(): DeliveryRequest? {
        return withContext(Dispatchers.IO) {
            try {
                val date = sharedPreferences.getString(KEY_PICKUP_DATE, null)
                val instructions = sharedPreferences.getString(KEY_PICKUP_INSTRUCTIONS, null)

                if (date != null && instructions != null) {
                    DeliveryRequest(
                        customerId = "12345", // Replace with actual logic to get the customer ID
                        address = "Customer Address", // Replace with actual logic to get the address
                        date = date,
                        instructions = instructions
                    )
                } else null
            } catch (e: Exception) {
                // Log or handle the error
                throw Exception("Failed to retrieve last pickup request: ${e.message}")
            }
        }
    }

    /**
     * Saves the stop service date to SharedPreferences.
     * @param date The selected stop service date.
     */
    suspend fun saveServiceStopDate(date: String) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit().apply {
                    putString(KEY_STOP_SERVICE_DATE, date)
                    apply()
                }
            } catch (e: Exception) {
                // Log or handle the error
                throw Exception("Failed to save stop service date: ${e.message}")
            }
        }
    }

    /**
     * Retrieves the last saved stop service date from SharedPreferences.
     * @return The stop service date as a String, or null if not found.
     */
    suspend fun getStopServiceDate(): String? {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getString(KEY_STOP_SERVICE_DATE, null)
            } catch (e: Exception) {
                // Log or handle the error
                throw Exception("Failed to retrieve stop service date: ${e.message}")
            }
        }
    }

    /**
     * Clears the stop service date (temporary stop service).
     */
    suspend fun tempStopService() {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit().apply {
                    remove(KEY_STOP_SERVICE_DATE)
                    apply()
                }
            } catch (e: Exception) {
                // Log or handle the error
                throw Exception("Failed to temporarily stop service: ${e.message}")
            }
        }
    }
}
