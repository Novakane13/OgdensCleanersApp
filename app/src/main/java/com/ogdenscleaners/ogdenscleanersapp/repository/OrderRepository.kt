package com.ogdenscleaners.ogdenscleanersapp.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ogdenscleaners.ogdenscleanersapp.models.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(private val context: Context) {

    private val ordersList = mutableListOf<Order>()

    suspend fun getOrders(): List<Order> {
        // Placeholder logic: In real-world scenarios, this might interact with Firebase or Room
        return withContext(Dispatchers.IO) {
            // Simulated database fetch
            ordersList
        }package com.ogdenscleaners.ogdenscleanersapp.repository

        import android.content.Context
                import com.android.volley.Request
                import com.android.volley.toolbox.JsonObjectRequest
                import com.android.volley.toolbox.Volley
                import com.ogdenscleaners.ogdenscleanersapp.models.Order
                import kotlinx.coroutines.Dispatchers
                import kotlinx.coroutines.withContext
                import org.json.JSONObject
                import javax.inject.Inject
                import javax.inject.Singleton

                @Singleton
                class OrderRepository @Inject constructor(private val context: Context) {

                    private val ordersList = mutableListOf<Order>()

                    suspend fun getOrders(): List<Order> {
                        // Placeholder logic: In real-world scenarios, this might interact with Firebase or Room
                        return withContext(Dispatchers.IO) {
                            // Simulated database fetch
                            ordersList
                        }
                    }

                    suspend fun createPaymentIntent(amount: Int): JSONObject {
                        return withContext(Dispatchers.IO) {
                            val url = "http://10.0.2.2:4242/create-payment-intent"
                            val jsonObject = JSONObject().apply {
                                put("amount", amount)
                                put("currency", "usd")
                            }

                            val response = suspendCoroutine<JSONObject> { cont ->
                                val request = JsonObjectRequest(
                                    Request.Method.POST, url, jsonObject,
                                    { response -> cont.resume(response) },
                                    { error -> cont.resumeWithException(error) }
                                )
                                Volley.newRequestQueue(context).add(request)
                            }
                            response
                        }
                    }

                    fun updateOrders(activeOrders: List<Order>, inactiveOrders: List<Order>) {
                        // Update internal storage or remote database
                        ordersList.clear()
                        ordersList.addAll(activeOrders)
                        ordersList.addAll(inactiveOrders)
                    }
                }

    }

    suspend fun createPaymentIntent(amount: Int): JSONObject {
        return withContext(Dispatchers.IO) {
            val url = "http://10.0.2.2:4242/create-payment-intent"
            val jsonObject = JSONObject().apply {
                put("amount", amount)
                put("currency", "usd")
            }

            val response = suspendCoroutine<JSONObject> { cont ->
                val request = JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    { response -> cont.resume(response) },
                    { error -> cont.resumeWithException(error) }
                )
                Volley.newRequestQueue(context).add(request)
            }
            response
        }
    }

    fun updateOrders(activeOrders: List<Order>, inactiveOrders: List<Order>) {
        // Update internal storage or remote database
        ordersList.clear()
        ordersList.addAll(activeOrders)
        ordersList.addAll(inactiveOrders)
    }
}
