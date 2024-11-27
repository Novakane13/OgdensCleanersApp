package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityLoginBinding
import com.ogdenscleaners.ogdenscleanersapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Login button action
        binding.loginbutton.setOnClickListener {
            val email = binding.emailinput.text.toString().trim()
            val password = binding.passwordinput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        loginViewModel.loginStatus.observe(this, Observer { result ->
            result.onSuccess {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                // Navigate to DashboardActivity
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)

                // Optional: Finish LoginActivity to prevent going back to it
                finish()
            }.onFailure {
                Toast.makeText(this, "Login Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
