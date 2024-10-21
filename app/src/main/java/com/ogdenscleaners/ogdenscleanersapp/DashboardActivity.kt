package com.ogdenscleaners.ogdenscleanersapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.ogdenscleaners.ogdenscleanersapp.ui.theme.OgdensCleanersAppTheme
import kotlinx.coroutines.launch

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OgdensCleanersAppTheme {
                DashboardScreen { destination ->
                    navigateTo(destination)
                }
            }
        }
    }

    private fun navigateTo(destination: String) {
        val intent = when (destination) {
            "AccountInfo" -> Intent(this, AccountInfoActivity::class.java)
            "Orders" -> Intent(this, OrdersActivity::class.java)
            "Billing" -> Intent(this, BillingActivity::class.java)
            "Delivery" -> Intent(this, DeliveryActivity::class.java)
            "Notifications" -> Intent(this, NotificationsActivity::class.java)
            "Feedback" -> Intent(this, FeedbackActivity::class.java)
            "Logout" -> {
                FirebaseAuth.getInstance().signOut()
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
                return
            }
            else -> null
        }
        intent?.let { startActivity(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onNavigate: (String) -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerItems(onNavigate)
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->
            DashboardActions(onNavigate = onNavigate, modifier = Modifier.padding(padding))
        }
    }
}

@Composable
fun DrawerItems(onNavigate: (String) -> Unit) {
    Column {
        NavigationDrawerItem(
            label = { Text("Account Info") },
            selected = false,
            onClick = { onNavigate("AccountInfo") }
        )
        NavigationDrawerItem(
            label = { Text("Orders") },
            selected = false,
            onClick = { onNavigate("Orders") }
        )
        NavigationDrawerItem(
            label = { Text("Billing") },
            selected = false,
            onClick = { onNavigate("Billing") }
        )
        NavigationDrawerItem(
            label = { Text("Delivery") },
            selected = false,
            onClick = { onNavigate("Delivery") }
        )
        NavigationDrawerItem(
            label = { Text("Notifications") },
            selected = false,
            onClick = { onNavigate("Notifications") }
        )
        NavigationDrawerItem(
            label = { Text("Feedback") },
            selected = false,
            onClick = { onNavigate("Feedback") }
        )
        NavigationDrawerItem(
            label = { Text("Logout") },
            selected = false,
            onClick = { onNavigate("Logout") }
        )
    }
}

@Composable
fun DashboardActions(onNavigate: (String) -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = { onNavigate("AccountInfo") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Account Information")
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { onNavigate("Orders") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Orders")
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { onNavigate("Billing") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Billing")
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { onNavigate("Delivery") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delivery")
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { onNavigate("Notifications") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Notifications")
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { onNavigate("Feedback") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Feedback")
        }
    }
}
