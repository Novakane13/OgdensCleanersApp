package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.ogdenscleaners.ogdenscleanersapp.R.*
import com.ogdenscleaners.ogdenscleanersapp.adapters.CreditCardAdapter
import com.ogdenscleaners.ogdenscleanersapp.models.CreditCard
import org.json.JSONArray

class AccountInfoActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val savedCards: MutableList<CreditCard> = mutableListOf()

    // UI Elements
    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextNotes: EditText
    private lateinit var buttonSave: Button
    private lateinit var chipAddCard: Chip
    private lateinit var savedCardsRecyclerView: RecyclerView
    private lateinit var cardAdapter: CreditCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_account_info)

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)

        // Initialize views
        editTextName = findViewById(id.editTextName)
        editTextPhone = findViewById(id.editTextPhone)
        editTextEmail = findViewById(id.editTextEmail)
        editTextNotes = findViewById(id.editTextNotes)
        buttonSave = findViewById(id.buttonSave)
        chipAddCard = findViewById(id.chipAddCard)
        savedCardsRecyclerView = findViewById(id.savedCardsRecyclerView)

        // Load saved user info
        loadUserInfo()

        // Set up RecyclerView for saved credit cards
        cardAdapter = CreditCardAdapter(savedCards)
        savedCardsRecyclerView.layoutManager = LinearLayoutManager(this)
        savedCardsRecyclerView.adapter = cardAdapter

        // Load saved credit cards
        loadSavedCreditCards()

        // Set up the Add Card button click to open CreditCardActivity
        chipAddCard.setOnClickListener {
            val intent = Intent(this, CreditCardActivity::class.java)
            startActivity(intent)
        }

        // Handle Save Changes button click (for name, phone, email, notes)
        buttonSave.setOnClickListener {
            saveUserInfo()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the credit cards list when returning to this activity
        loadSavedCreditCards()
    }

    private fun saveUserInfo() {
        val name = editTextName.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val notes = editTextNotes.text.toString().trim()

        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("phone", phone)
        editor.putString("email", email)
        editor.putString("notes", notes)
        editor.apply()

        Toast.makeText(this, "User info saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadUserInfo() {
        editTextName.setText(sharedPreferences.getString("name", ""))
        editTextPhone.setText(sharedPreferences.getString("phone", ""))
        editTextEmail.setText(sharedPreferences.getString("email", ""))
        editTextNotes.setText(sharedPreferences.getString("notes", ""))
    }

    private fun loadSavedCreditCards() {
        savedCards.clear()
        val cardsJson = sharedPreferences.getString("cards", null)
        if (cardsJson != null) {
            val jsonArray = JSONArray(cardsJson)
            for (i in 0 until jsonArray.length()) {
                val cardJson = jsonArray.getJSONObject(i)
                val card = CreditCard(
                    cardholderName = cardJson.getString("cardholderName"),
                    lastFourDigits = cardJson.getString("lastFourDigits"),
                    expiryDate = cardJson.getString("expiryDate")
                )
                savedCards.add(card)
            }
        }
        // Since the entire list is being reloaded, use submitList() for efficiency
        cardAdapter.notifyItemRangeChanged(0, savedCards.size)
    }

}
