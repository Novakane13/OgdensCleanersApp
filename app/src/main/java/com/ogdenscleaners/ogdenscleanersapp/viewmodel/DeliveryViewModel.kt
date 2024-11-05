package com.ogdenscleaners.ogdenscleanersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogdenscleaners.ogdenscleanersapp.models.PickupRequest
import com.ogdenscleaners.ogdenscleanersapp.repository.DeliveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository
) : ViewModel() {

    private val _pickupRequest = MutableLiveData<PickupRequest?>()
    val pickupRequest: LiveData<PickupRequest?> get() = _pickupRequest

    private val _serviceStopDate = MutableLiveData<String?>()
    val serviceStopDate: LiveData<String?> get() = _serviceStopDate

    fun stopService(selectedDate: String) {
        viewModelScope.launch {
            deliveryRepository.saveServiceStopDate(selectedDate)
            _serviceStopDate.value = selectedDate
        }
    }

    fun requestPickup(pickupRequest: PickupRequest) {
        viewModelScope.launch {
            deliveryRepository.savePickupRequest(pickupRequest)
            _pickupRequest.value = pickupRequest
        }
    }

    fun tempStopService() {
        viewModelScope.launch {
            deliveryRepository.tempStopService()
        }
    }
}
