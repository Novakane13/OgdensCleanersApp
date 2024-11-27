package com.ogdenscleaners.ogdenscleanersapp.repository

import com.ogdenscleaners.ogdenscleanersapp.api.ApiService
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement
import com.ogdenscleaners.ogdenscleanersapp.models.PaymentIntentRequest
import com.ogdenscleaners.ogdenscleanersapp.models.PaymentIntentResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getBillingStatements(): List<BillingStatement> {
        return listOf(
            BillingStatement(
                statementId = "1",
                customerId = "CUST001",
                date = "January 2024",
                amountOwed = "100.00",
                orderIds = listOf("ORD001", "ORD002"), // Provide a List<String> for order IDs
                paidStatus = false
            ),
            BillingStatement(
                statementId = "2",
                customerId = "CUST002",
                date = "February 2024",
                amountOwed = "80.00",
                orderIds = listOf("ORD003", "ORD004"), // Provide a List<String> for order IDs
                paidStatus = true
            )
        )
    }

    suspend fun createPaymentIntent(
        amount: Int,
        customerId: String,
        paymentMethodId: String
    ): PaymentIntentResponse {
        val paymentIntentRequest = PaymentIntentRequest(
            amount = amount,
            currency = "usd",
            customerId = customerId,
            paymentMethodId = paymentMethodId
        )
        return apiService.createPaymentIntent(paymentIntentRequest).execute().body()!!
    }
}
