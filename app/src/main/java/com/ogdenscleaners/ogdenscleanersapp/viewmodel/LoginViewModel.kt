package com.ogdenscleaners.ogdenscleanersapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogdenscleaners.ogdenscleanersapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val loginStatus = authRepository.loginStatus

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            authRepository.loginUser(email, password)
        }
    }

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            authRepository.registerUser(email, password)
        }
    }
}
