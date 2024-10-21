package com.ogdenscleaners.ogdenscleanersapp

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
import com.google.firebase.auth.UserProfileChangeRequest
import com.ogdenscleaners.ogdenscleanersapp.activities.DashboardActivity
import com.ogdenscleaners.ogdenscleanersapp.activities.LoginActivity
import com.ogdenscleaners.ogdenscleanersapp.ui.theme.OgdensCleanersAppTheme

class RegisterActivity : ComponentActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        setContent {
            OgdensCleanersAppTheme {
                RegisterScreen(
                    onRegisterSuccess = { navigateToDashboard() },
                    onNavigateToLogin = { navigateToLogin() },
                    mAuth = mAuth
                )
            }
        }
    }

    private fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    mAuth: FirebaseAuth
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Register") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
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
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (validateInputs(context, name, email, password, confirmPassword)) {
                        registerUser(
                            context,
                            mAuth,
                            name,
                            email,
                            password,
                            onRegisterSuccess
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Already have an account? Login",
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}

private fun validateInputs(
    context: android.content.Context,
    name: String,
    email: String,
    password: String,
    confirmPassword: String
): Boolean {
    return when {
        name.isEmpty() -> {
            showToast(context, "Name is required")
            false
        }
        email.isEmpty() -> {
            showToast(context, "Email is required")
            false
        }
        password.isEmpty() -> {
            showToast(context, "Password is required")
            false
        }
        password != confirmPassword -> {
            showToast(context, "Passwords do not match")
            false
        }
        else -> true
    }
}

private fun registerUser(
    context: android.content.Context,
    mAuth: FirebaseAuth,
    name: String,
    email: String,
    password: String,
    onRegisterSuccess: () -> Unit
) {
    mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                    if (profileTask.isSuccessful) {
                        showToast(context, "Registration successful")
                        onRegisterSuccess()
                    }
                }
            } else {
                showToast(context, "Registration failed: ${task.exception?.message}")
            }
        }
}

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
