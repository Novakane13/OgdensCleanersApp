package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.ogdenscleaners.ogdenscleanersapp.R

class RegisterActivity : ComponentActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var employeePinEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize views
        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        phoneEditText = findViewById(R.id.editTextPhone)
        passwordEditText = findViewById(R.id.Passwordinput)
        confirmPasswordEditText = findViewById(R.id.confirmpassword)
        employeePinEditText = findViewById(R.id.editTextEmployeePin)
        registerButton = findViewById(R.id.buttonRegister)

        // Register button action
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val employeePin = employeePinEditText.text.toString()

            if (validateInputs(name, email, password, confirmPassword)) {
                registerUser(name, email, password, employeePin)
            }
        }
    }

    private fun validateInputs(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        return when {
            name.isEmpty() -> {
                showToast("Name is required")
                false
            }
            email.isEmpty() -> {
                showToast("Email is required")
                false
            }
            password.isEmpty() -> {
                showToast("Password is required")
                false
            }
            password != confirmPassword -> {
                showToast("Passwords do not match")
                false
            }
            else -> true
        }
    }

    private fun registerUser(name: String, email: String, password: String, employeePin: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val isEmployee = employeePin == "1313"  // Employee PIN to differentiate

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                        if (profileTask.isSuccessful) {
                            saveUserType(user.uid, isEmployee)
                            showToast("Registration successful")
                            navigateToDashboard(isEmployee)
                        }
                    }
                } else {
                    showToast("Registration failed: ${task.exception?.message}")
                }
            }
    }

    private fun saveUserType(uid: String, isEmployee: Boolean) {
        // Save user type to Firestore
        val db = FirebaseFirestore.getInstance()
        val userRole = if (isEmployee) "employee" else "customer"
        val userMap = hashMapOf("role" to userRole)

        db.collection("users").document(uid)
            .set(userMap)
            .addOnSuccessListener {
                showToast("User role saved")
            }
            .addOnFailureListener {
                showToast("Failed to save user role")
            }
    }

    private fun navigateToDashboard(isEmployee: Boolean) {
        val intent = if (isEmployee) {
            Intent(this, EmployeeDashboardActivity::class.java)  // Launch employee dashboard
        } else {
            Intent(this, DashboardActivity::class.java)  // Launch normal user dashboard
        }
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
