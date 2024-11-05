package com.ogdenscleaners.ogdenscleanersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import com.ogdenscleaners.ogdenscleanersapp.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _activeOrders = MutableLiveData<List<Order>>()
    val activeOrders: LiveData<List<Order>> get() = _activeOrders

    private val _inactiveOrders = MutableLiveData<List<Order>>()
    val inactiveOrders: LiveData<List<Order>> get() = _inactiveOrders

    private val _paymentIntentClientSecret = MutableLiveData<String?>()
    val paymentIntentClientSecret: LiveData<String?> get() = _paymentIntentClientSecret

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadOrders() {
        viewModelScope.launch {
            val orders = orderRepository.getOrders() // Get orders from the repository
            _activeOrders.value = orders.filter { !it.isReadyForPickup }
            _inactiveOrders.value = orders.filter { it.isReadyForPickup }
        }
    }

    fun initiatePayment(selectedOrders: List<Order>) {
        val totalAmount = selectedOrders.sumOf { it.total * 100 }.toInt() // Amount in cents
        createPaymentIntent(totalAmount)
    }

    private fun createPaymentIntent(amount: Int) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = orderRepository.createPaymentIntent(amount)
                _paymentIntentClientSecret.value = response.getString("clientSecret")
            } catch (e: Exception) {
                _errorMessage.value = "Error creating payment intent: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun completePayment(selectedOrders: List<Order>) {
        selectedOrders.forEach { it.isReadyForPickup = true }
        val updatedActiveOrders = _activeOrders.value?.toMutableList() ?: mutableListOf()
        updatedActiveOrders.removeAll(selectedOrders)
        _activeOrders.value = updatedActiveOrders

        val updatedInactiveOrders = _inactiveOrders.value?.toMutableList() ?: mutableListOf()
        updatedInactiveOrders.addAll(selectedOrders)
        _inactiveOrders.value = updatedInactiveOrders

        orderRepository.updateOrders(_activeOrders.value.orEmpty(), _inactiveOrders.value.orEmpty())
    }
}
