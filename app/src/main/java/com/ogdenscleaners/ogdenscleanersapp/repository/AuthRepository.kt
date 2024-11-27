package com.ogdenscleaners.ogdenscleanersapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth // Injected instance of FirebaseAuth
) {
    private val _loginStatus = MutableLiveData<Result<Boolean>>()
    val loginStatus: LiveData<Result<Boolean>> get() = _loginStatus

    suspend fun loginUser(email: String, password: String) {
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            // If we reach here, it means the login was successful
            _loginStatus.postValue(Result.success(true))
        } catch (e: Exception) {
            _loginStatus.postValue(Result.failure(e))
        }
    }

    suspend fun registerUser(email: String, password: String) {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            // If we reach here, it means registration was successful
            _loginStatus.postValue(Result.success(true))
        } catch (e: Exception) {
            _loginStatus.postValue(Result.failure(e))
        }
    }
}
