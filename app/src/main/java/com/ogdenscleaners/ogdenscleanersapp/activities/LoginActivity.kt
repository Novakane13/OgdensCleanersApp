package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ogdenscleaners.ogdenscleanersapp.R

class LoginActivity : ComponentActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.passwordinput)
        loginButton = findViewById(R.id.buttonLogin)
        registerTextView = findViewById(R.id.textViewRegister)


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please fill out both email and password")
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            if (user != null) {
                                fetchUserRoleAndNavigate(user.uid)
                            }
                        } else {
                            showToast("Authentication Failed: ${task.exception?.message}")
                        }
                    }
            }
        }

        // Register link action
        registerTextView.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun fetchUserRoleAndNavigate(uid: String) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val role = document.getString("role")
                    if (role == "employee") {
                        navigateToDashboard(true)
                    } else {
                        navigateToDashboard(false)
                    }
                } else {
                    showToast("User role not found")
                }
            }
            .addOnFailureListener {
                showToast("Failed to fetch user role")
            }
    }

    private fun navigateToDashboard(isEmployee: Boolean) {
        val intent = if (isEmployee) {
            Intent(this, EmployeeDashboardActivity::class.java)
        } else {
            Intent(this, DashboardActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
