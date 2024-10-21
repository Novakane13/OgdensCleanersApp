package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.ogdenscleaners.ogdenscleanersapp.RegisterActivity
import com.ogdenscleaners.ogdenscleanersapp.ui.theme.OgdensCleanersAppTheme

class LoginActivity : ComponentActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        setContent {
            OgdensCleanersAppTheme {
                LoginScreen(
                    onLoginSuccess = { navigateToDashboard() },
                    onNavigateToRegister = { navigateToRegister() },
                    mAuth = mAuth
                )
            }
        }
    }

    private fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    mAuth: FirebaseAuth
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        showToast(context, "Please fill out both email and password")
                    } else {
                        mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    onLoginSuccess()
                                } else {
                                    showToast(
                                        context,
                                        "Authentication Failed: ${task.exception?.message}"
                                    )
                                }
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Don't have an account? Register",
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
