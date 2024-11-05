package com.ogdenscleaners.ogdenscleanersapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    fun registerUser(
        name: String,
        email: String,
        password: String,
        employeePin: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val isEmployee = employeePin == "1313"

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                        if (profileTask.isSuccessful) {
                            saveUserType(user.uid, isEmployee, onSuccess, onFailure)
                        }
                    }
                } else {
                    onFailure(task.exception?.message ?: "Registration failed")
                }
            }
    }

    private fun saveUserType(
        uid: String,
        isEmployee: Boolean,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userRole = if (isEmployee) "employee" else "customer"
        val userMap = hashMapOf("role" to userRole)

        db.collection("users").document(uid)
            .set(userMap)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Failed to save user role")
            }
    }
}
