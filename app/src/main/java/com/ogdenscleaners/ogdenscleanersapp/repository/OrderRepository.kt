package com.ogdenscleaners.ogdenscleanersapp.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class OrderRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val ordersList = mutableListOf<Order>()

    suspend fun getActiveOrders(): List<Order> = withContext(Dispatchers.IO) {
        ordersList.filter { it.pickedup && it.paidfor }
    }

    suspend fun getInactiveOrders(): List<Order> = withContext(Dispatchers.IO) {
        ordersList.filter { !it.pickedup || !it.paidfor }
    }

    suspend fun createPaymentIntent(amount: Int): String = withContext(Dispatchers.IO) {
        val url = "http://10.0.2.2:4242/create-payment-intent"
        val jsonObject = JSONObject().apply {
            put("amount", amount)
            put("currency", "usd")
        }

        suspendCoroutine { cont ->
            val request = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response -> cont.resume(response.getString("clientSecret")) },
                { error -> cont.resumeWithException(error) }
            )
            Volley.newRequestQueue(context).add(request)
        }
    }

    fun updateOrders(activeOrders: List<Order>, inactiveOrders: List<Order>) {
        ordersList.clear()
        ordersList.addAll(activeOrders)
        ordersList.addAll(inactiveOrders)
    }
}
