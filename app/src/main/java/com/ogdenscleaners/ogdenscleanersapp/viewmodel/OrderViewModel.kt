package com.ogdenscleaners.ogdenscleanersapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import com.ogdenscleaners.ogdenscleanersapp.repositories.OrderRepository
import com.stripe.android.paymentsheet.PaymentSheet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val paymentSheet: PaymentSheet
) : ViewModel() {

    private val _activeOrders = MutableLiveData<List<Order>>()
    val activeOrders: LiveData<List<Order>> = _activeOrders

    private val _inactiveOrders = MutableLiveData<List<Order>>()
    val inactiveOrders: LiveData<List<Order>> = _inactiveOrders

    private val _orderDetails = MutableLiveData<String?>()
    val orderDetails: LiveData<String?> = _orderDetails

    private val _paymentResult = MutableLiveData<String>()
    val paymentResult: LiveData<String> = _paymentResult

    init {
        loadOrders()
    }

    private fun loadOrders() {
        // Load active and inactive orders from repository
        _activeOrders.value = orderRepository.getActiveOrders()
        _inactiveOrders.value = orderRepository.getInactiveOrders()
    }

    fun showOrderDetails(order: Order) {
        val details = """
            Order ID: ${order.id}
            Drop Off Date: ${order.date}
            Items: ${order.items.joinToString(", ")}
            Total Pieces: ${order.items.size}
            Total Cost: $${order.total}
            Status: ${if (order.isReadyForPickup) "Ready for Pickup" else "Not Ready for Pickup"}
        """.trimIndent()
        _orderDetails.value = details
    }

    fun requestOrderDetails(selectedOrders: List<Order>) {
        if (selectedOrders.size == 1) {
            showOrderDetails(selectedOrders.first())
        } else {
            _paymentResult.value = "Please select exactly one order to view details"
        }
    }

    fun initiatePayment(selectedOrders: List<Order>) {
        if (selectedOrders.isNotEmpty()) {
            val totalAmount = selectedOrders.sumOf { it.total * 100 }.toInt()
            createPaymentIntent(totalAmount)
        } else {
            _paymentResult.value = "Please select at least one order to make a payment"
        }
    }

    private fun createPaymentIntent(amount: Int) {
        viewModelScope.launch {
            val clientSecret = orderRepository.createPaymentIntent(amount)
            clientSecret?.let {
                paymentSheet.presentWithPaymentIntent(
                    it,
                    PaymentSheet.Configuration(merchantDisplayName = "Ogden's Cleaners")
                )
            } ?: run {
                _paymentResult.value = "Error creating payment intent"
            }
        }
    }
}
