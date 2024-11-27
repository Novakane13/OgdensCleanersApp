package com.ogdenscleaners.ogdenscleanersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogdenscleaners.ogdenscleanersapp.models.DeliveryRequest
import com.ogdenscleaners.ogdenscleanersapp.repository.DeliveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository
) : ViewModel() {

    private val _pickupRequest = MutableLiveData<DeliveryRequest?>()
    val pickupRequest: LiveData<DeliveryRequest?> get() = _pickupRequest

    private val _serviceStopDate = MutableLiveData<String?>()
    val serviceStopDate: LiveData<String?> get() = _serviceStopDate

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    /**
     * Stops the delivery service starting from the selected date.
     */
    fun stopService(selectedDate: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                deliveryRepository.saveServiceStopDate(selectedDate) // Call the repository method
                _serviceStopDate.value = selectedDate // Update LiveData
            } catch (e: Exception) {
                _errorMessage.value = "Failed to stop service: ${e.localizedMessage}" // Handle error
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Requests a pickup based on the provided delivery request details.
     */
    fun requestPickup(pickupRequest: DeliveryRequest) {
        viewModelScope.launch {
            _loading.value = true
            try {
                deliveryRepository.savePickupRequest(pickupRequest) // Call the repository method
                _pickupRequest.value = pickupRequest // Update LiveData
            } catch (e: Exception) {
                _errorMessage.value = "Failed to request pickup: ${e.localizedMessage}" // Handle error
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Temporarily stops the delivery service.
     */
    fun tempStopService() {
        viewModelScope.launch {
            _loading.value = true
            try {
                deliveryRepository.tempStopService() // Call the repository method
            } catch (e: Exception) {
                _errorMessage.value = "Failed to temporarily stop service: ${e.localizedMessage}" // Handle error
            } finally {
                _loading.value = false
            }
        }
    }
}
