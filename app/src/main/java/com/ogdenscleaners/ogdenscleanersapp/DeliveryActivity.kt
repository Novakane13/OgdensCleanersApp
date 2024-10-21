package com.ogdenscleaners.ogdenscleanersapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.ogdenscleaners.ogdenscleanersapp.api.ApiClient
import com.ogdenscleaners.ogdenscleanersapp.api.ApiService
import com.ogdenscleaners.ogdenscleanersapp.models.DeliveryRequest
import com.ogdenscleaners.ogdenscleanersapp.models.DeliveryResponse
import com.ogdenscleaners.ogdenscleanersapp.ui.theme.OgdensCleanersAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryActivity : ComponentActivity() {
    private lateinit var apiService: ApiService
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = ApiClient.retrofitInstance.create(ApiService::class.java)
        userId = FirebaseAuth.getInstance().currentUser?.uid

        setContent {
            OgdensCleanersAppTheme {
                DeliveryScreen(apiService = apiService, userId = userId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(apiService: ApiService, userId: String?) {
    var address by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Delivery Request") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = { Text("Instructions") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (address.isEmpty() || date.isEmpty() || time.isEmpty()) {
                        showToast(context, "Please fill in all required fields")
                    } else {
                        val deliveryRequest = DeliveryRequest(
                            userId = userId ?: "",
                            address = address,
                            date = date,
                            time = time,
                            instructions = instructions
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            submitDeliveryRequest()
                        }

                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}

fun submitDeliveryRequest() {
    TODO("Not yet implemented")
}

private suspend fun submitDeliveryRequest(
    apiService: ApiService,
    deliveryRequest: DeliveryRequest,
    context: android.content.Context
) {
    apiService.requestDelivery(deliveryRequest).enqueue(object : Callback<DeliveryResponse> {
        override fun onResponse(call: Call<DeliveryResponse>, response: Response<DeliveryResponse>) {
            if (response.isSuccessful) {
                showToast(context, "Delivery request submitted successfully")
            } else {
                showToast(context, "Failed to submit delivery request")
            }
        }

        override fun onFailure(call: Call<DeliveryResponse>, t: Throwable) {
            showToast(context, "Error: ${t.message}")
        }
    })
}

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
