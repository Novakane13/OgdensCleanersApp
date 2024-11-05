package com.ogdenscleaners.ogdenscleanersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement
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

    fun loadBillingStatements() {
        viewModelScope.launch {
            _billingStatements.value = billingRepository.getBillingStatements()
        }
    }

    fun initiatePayment(selectedStatements: List<BillingStatement>, onError: (String) -> Unit) {
        val totalAmount = selectedStatements.sumOf { it.totalAmount.toDouble() * 100 }.toInt()
        viewModelScope.launch {
            try {
                val clientSecret = billingRepository.createPaymentIntent(totalAmount)
                _paymentIntentClientSecret.value = clientSecret
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error creating payment intent")
            }
        }
    }

    fun markStatementsPaid(statements: List<BillingStatement>) {
        val updatedBillingStatements = _billingStatements.value?.map {
            if (statements.contains(it)) it.copy(paidStatus = true) else it
        }
        _billingStatements.value = updatedBillingStatements
    }
}
