package com.ogdenscleaners.ogdenscleanersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import com.ogdenscleaners.ogdenscleanersapp.repository.BillingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val billingRepository: BillingRepository
) : ViewModel() {

    private val _billingStatements = MutableLiveData<List<BillingStatement>>()
    val billingStatements: LiveData<List<BillingStatement>> get() = _billingStatements

    private val _paymentIntentClientSecret = MutableLiveData<String?>()
    val paymentIntentClientSecret: LiveData<String?> get() = _paymentIntentClientSecret

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _orders = MutableLiveData<Result<List<Order>>>()
    val orders: LiveData<Result<List<Order>>> get() = _orders


    fun loadBillingStatements() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val statements = billingRepository.getBillingStatements()
                _billingStatements.value = statements
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load billing statements: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun initiatePayment(
        selectedStatements: List<BillingStatement>,
        customerId: String,
        paymentMethodId: String
    ): LiveData<Result<String>> {
        val totalAmount = selectedStatements.sumOf { it.amountOwed.toDouble() * 100 }.toInt()
        val result = MutableLiveData<Result<String>>()

        viewModelScope.launch {
            try {
                val paymentIntentResponse = billingRepository.createPaymentIntent(
                    totalAmount,
                    customerId,
                    paymentMethodId
                )
                val clientSecret = paymentIntentResponse.clientSecret
                _paymentIntentClientSecret.value = clientSecret  // Optional, for direct observation
                result.value = Result.success(clientSecret)
            } catch (e: Exception) {
                _errorMessage.value = "Error creating payment intent: ${e.localizedMessage}"
                result.value = Result.failure(e)
            }
        }

        return result
    }
    fun setMockOrders(mockOrders: List<Order>) {
        // Update the LiveData with mock orders
        _billingStatements.value = mockOrders.map { order ->
            BillingStatement(
                statementId = order.id,
                customerId = order.customerId,
                date = order.dropOffDate,
                amountOwed = order.orderTotal.toString(),
                orderIds = listOf(order.id),
                paidStatus = false // Mock data, adjust as needed
            )
        }
    }
    fun markStatementsPaid(statements: List<BillingStatement>) {
        viewModelScope.launch {
            val updatedStatements = _billingStatements.value?.map {
                if (it in statements) it.copy(paidStatus = true) else it
            } ?: emptyList()
            _billingStatements.value = updatedStatements


        }
    }
}

