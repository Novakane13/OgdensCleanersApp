package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityRegisterBinding
import com.ogdenscleaners.ogdenscleanersapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.Passwordinput.text.toString()
            val confirmPassword = binding.confirmpassword.text.toString()
            val employeePin = binding.editTextEmployeePin.text.toString()

            if (validateInputs(name, email, password, confirmPassword)) {
                registerViewModel.registerUser(name, email, password, employeePin,
                    onSuccess = {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        // Navigate to Dashboard
                    },
                    onFailure = { error ->
                        Toast.makeText(this, "Registration failed: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun validateInputs(name: String, email: String, password: String, confirmPassword: String): Boolean {
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
