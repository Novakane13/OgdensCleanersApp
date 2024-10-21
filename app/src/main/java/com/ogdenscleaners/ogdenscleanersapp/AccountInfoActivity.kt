package com.ogdenscleaners.ogdenscleanersapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ogdenscleaners.ogdenscleanersapp.ui.theme.OgdensCleanersAppTheme
import androidx.compose.ui.tooling.preview.Preview

class AccountInfoActivity : ComponentActivity() {
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = FirebaseAuth.getInstance().currentUser?.uid

        setContent {
            OgdensCleanersAppTheme {
                userId?.let { uid ->
                    AccountScreen(uid)
                }
            }
        }
    }
}

@Composable
fun AccountScreen(userId: String) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var savedCards by remember { mutableStateOf(listOf<Pair<String, String>>()) }

    val context = LocalContext.current
    val db = Firebase.firestore

    // Fetch user data from Firestore
    LaunchedEffect(userId) {
        db.collection("accounts").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    firstName = document.getString("firstName") ?: ""
                    lastName = document.getString("lastName") ?: ""
                    email = document.getString("email") ?: ""
                    phone = document.getString("phone") ?: ""
                }
            }
            .addOnFailureListener {
                showToast(context, "Failed to load account information")
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo at the top center
        Image(
            painter = painterResource(id = R.drawable.logo2),
            contentDescription = "Logo",
            modifier = Modifier
                .height(100.dp)
                .padding(bottom = 16.dp)
        )

        // First and Last Name side by side
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Phone and Email fields
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Add Credit/Debit Card button
        Button(
            onClick = { /* Navigate to Add Card Screen */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))
        ) {
            Text("Add Credit/Debit Card", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display saved credit/debit cards
        Text("Saved Cards", fontSize = 18.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        savedCards.forEach { card ->
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Card ending in ${card.first}")
                    Text(text = "Exp: ${card.second}")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Save button
        Button(
            onClick = {
                saveAccountInformation(
                    userId = userId,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    phone = phone,
                    context = context
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))
        ) {
            Text("Save", color = Color.White)
        }
    }
}

fun saveAccountInformation(
    userId: String,
    firstName: String,
    lastName: String,
    email: String,
    phone: String,
    context: android.content.Context
) {
    val db = Firebase.firestore
    val account = hashMapOf(
        "firstName" to firstName,
        "lastName" to lastName,
        "email" to email,
        "phone" to phone
    )

    db.collection("accounts").document(userId)
        .set(account)
        .addOnSuccessListener {
            showToast(context, "Account information saved successfully!")
        }
        .addOnFailureListener {
            showToast(context, "Failed to save account information")
        }
}

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun PreviewAccountScreen() {
    // Use mock data to preview the layout
    AccountScreenPreview()
}

@Composable
fun AccountScreenPreview() {
    var firstName by remember { mutableStateOf("John") }
    var lastName by remember { mutableStateOf("Doe") }
    var email by remember { mutableStateOf("johndoe@example.com") }
    var phone by remember { mutableStateOf("(123) 456-7890") }
    var savedCards by remember { mutableStateOf(listOf("1234" to "12/24", "5678" to "11/23")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo at the top center
        Image(
            painter = painterResource(id = R.drawable.logo2),
            contentDescription = "Logo",
            modifier = Modifier
                .height(100.dp)
                .padding(bottom = 16.dp)
        )

        // First and Last Name side by side
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Phone and Email fields
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Add Credit/Debit Card button
        Button(
            onClick = { /* Mock action */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))
        ) {
            Text("Add Credit/Debit Card", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display saved credit/debit cards
        Text("Saved Cards", fontSize = 18.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        savedCards.forEach { card ->
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Card ending in ${card.first}")
                    Text(text = "Exp: ${card.second}")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Save button
        Button(
            onClick = { /* Mock action */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))
        ) {
            Text("Save", color = Color.White)
        }
    }
}
