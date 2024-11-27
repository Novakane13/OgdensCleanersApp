package com.ogdenscleaners.ogdenscleanersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import com.ogdenscleaners.ogdenscleanersapp.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val active = orderRepository.getActiveOrders()
                val inactive = orderRepository.getInactiveOrders()
                _activeOrders.value = active
                _inactiveOrders.value = inactive
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load orders: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun initiatePayment(selectedOrders: List<Order>) {
        if (selectedOrders.isNotEmpty()) {
            val totalAmount = selectedOrders.sumOf { it.orderTotal * 100 }.toInt()

            createPaymentIntent(amount = totalAmount)

        } else {
            _errorMessage.value = "Please select at least one order to make a payment"
        }
    }

    private fun createPaymentIntent(amount: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val clientSecret = orderRepository.createPaymentIntent(amount)
                _paymentIntentClientSecret.value = clientSecret
            } catch (e: Exception) {
                _errorMessage.value = "Error creating payment intent: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun completePayment(selectedOrders: List<Order>) {
        viewModelScope.launch {
            // Mark the orders as paid or update their status
            _activeOrders.value = _activeOrders.value?.filter { it !in selectedOrders }
            _inactiveOrders.value = _inactiveOrders.value?.plus(selectedOrders)
        }
    }
}
