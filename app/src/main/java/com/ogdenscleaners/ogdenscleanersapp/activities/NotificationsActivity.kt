package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.messaging.FirebaseMessaging
import com.ogdenscleaners.ogdenscleanersapp.ui.theme.OgdensCleanersAppTheme

class NotificationsActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isSubscribed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("ogdens_cleaners_prefs", Context.MODE_PRIVATE)
        isSubscribed = sharedPreferences.getBoolean("isSubscribed", false)

        setContent {
            OgdensCleanersAppTheme {
                NotificationsScreen(
                    isSubscribed = isSubscribed,
                    onSubscribeChange = { isChecked ->
                        if (isChecked) {
                            subscribeToNotifications()
                        } else {
                            unsubscribeFromNotifications()
                        }
                    }
                )
            }
        }
    }

    private fun subscribeToNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("order_updates")
            .addOnCompleteListener { task ->
                val context = this
                if (task.isSuccessful) {
                    isSubscribed = true
                    saveSubscriptionState(isSubscribed)
                    Toast.makeText(context, "Subscribed to notifications", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to subscribe", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun unsubscribeFromNotifications() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("order_updates")
            .addOnCompleteListener { task ->
                val context = this
                if (task.isSuccessful) {
                    isSubscribed = false
                    saveSubscriptionState(isSubscribed)
                    Toast.makeText(context, "Unsubscribed from notifications", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to unsubscribe", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveSubscriptionState(isSubscribed: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean("isSubscribed", isSubscribed)
            apply()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    isSubscribed: Boolean,
    onSubscribeChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var checked by remember { mutableStateOf(isSubscribed) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Enable notifications for order updates")
            Switch(
                checked = checked,
                onCheckedChange = { isChecked ->
                    checked = isChecked
                    onSubscribeChange(isChecked)
                    Toast.makeText(
                        context,
                        if (isChecked) "Subscribed to notifications" else "Unsubscribed from notifications",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
    }
}