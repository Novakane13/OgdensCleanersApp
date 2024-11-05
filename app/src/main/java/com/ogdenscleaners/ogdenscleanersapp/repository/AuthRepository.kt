package com.ogdenscleaners.ogdenscleanersapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth


class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _loginStatus = MutableLiveData<Result<Boolean>>()
    val loginStatus: LiveData<Result<Boolean>> get() = _loginStatus

    suspend fun loginUser(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginStatus.value = Result.success(true)
                } else {
                    _loginStatus.value = Result.failure(task.exception ?: Exception("Unknown Error"))
                }
            }
        } catch (e: Exception) {
            _loginStatus.value = Result.failure(e)
        }
    }

    suspend fun registerUser(email: String, password: String) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginStatus.value = Result.success(true)
                } else {
                    _loginStatus.value = Result.failure(task.exception ?: Exception("Unknown Error"))
                }
            }
        } catch (e: Exception) {
            _loginStatus.value = Result.failure(e)
        }
    }
}