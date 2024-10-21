package com.ogdenscleaners.ogdenscleanersapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ogdenscleaners.ogdenscleanersapp.models.Feedback
import com.ogdenscleaners.ogdenscleanersapp.ui.theme.OgdensCleanersAppTheme

class FeedbackActivity : ComponentActivity() {
    private lateinit var databaseReference: DatabaseReference
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("Feedback")
        // Get current user ID
        userId = FirebaseAuth.getInstance().currentUser?.uid

        setContent {
            OgdensCleanersAppTheme {
                FeedbackScreen(databaseReference, userId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(databaseReference: DatabaseReference, userId: String?) {
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current // Correct the reference

    Scaffold(
        topBar = { TopAppBar(title = { Text("Feedback") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Your Feedback") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (message.isNotEmpty()) {
                        val feedback = Feedback(
                            userId = userId ?: "Anonymous",
                            message = message,
                            timestamp = System.currentTimeMillis()
                        )
                        submitFeedback(databaseReference, feedback, context) {
                            message = "" // Clear the input after submission
                        }
                    } else {
                        showToast(context, "Please enter your feedback")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}

private fun submitFeedback(
    databaseReference: DatabaseReference,
    feedback: Feedback,
    context: android.content.Context,
    onSuccess: () -> Unit
) {
    val feedbackId = databaseReference.push().key
    if (feedbackId != null) {
        databaseReference.child(feedbackId).setValue(feedback)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(context, "Thank you for your feedback!")
                    onSuccess()
                } else {
                    showToast(context, "Failed to submit feedback")
                }
            }
    } else {
        showToast(context, "Failed to generate feedback ID")
    }
}

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
