package com.ogdenscleaners.ogdenscleanersapp.repository

import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getBillingStatements(): List<BillingStatement> {
        // Placeholder for fetching billing statements from server or database
        return listOf(
            BillingStatement("1", "January 2024", "100.00", "Monthly dry cleaning charges", "Billing statement for the Month of January", false),
            BillingStatement("2", "February 2024", "80.00", "Monthly dry cleaning charges", "Billing Statement for the Month of February", true)
        )
    }

    suspend fun createPaymentIntent(amount: Int): String {
        val jsonObject = JSONObject().apply {
            put("amount", amount)
            put("currency", "usd")
        }
        return apiService.createPaymentIntent(jsonObject)
    }
}
