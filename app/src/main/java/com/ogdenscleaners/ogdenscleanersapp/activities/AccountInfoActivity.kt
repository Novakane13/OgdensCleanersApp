package com.ogdenscleaners.ogdenscleanersapp.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.ogdenscleaners.ogdenscleanersapp.R

class AccountInfoActivity : AppCompatActivity() {

    private lateinit var savedCardsContainer: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private val savedCards: MutableList<String> = mutableListOf() // List to store credit card numbers

    // UI Elements
    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextNotes: EditText
    private lateinit var buttonSave: Button
    private lateinit var chipAddCard: Chip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)

        sharedPreferences = getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)

        // Initialize views
        editTextName = findViewById(R.id.editTextName)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextNotes = findViewById(R.id.editTextNotes)
        buttonSave = findViewById(R.id.buttonSave)
        chipAddCard = findViewById(R.id.chipAddCard)
        savedCardsContainer = findViewById(R.id.savedCardsContainer)

        // Load saved user info
        loadUserInfo()

        // Load saved credit cards
        loadSavedCreditCards()

        // Handle Add Card button click
        chipAddCard.setOnClickListener {
            showAddCardDialog()
        }

        // Handle Save Changes button click (for name, phone, email, notes)
        buttonSave.setOnClickListener {
            saveUserInfo()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showAddCardDialog() {
        val dialog = Dialog(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.new_credit_card, null)
        dialog.setContentView(dialogView)

        val editTextCardNumber = dialogView.findViewById<EditText>(R.id.editTextCardNumber)
        val buttonSaveCard = dialogView.findViewById<Button>(R.id.buttonSaveCard)

        buttonSaveCard.setOnClickListener {
            val cardNumber = editTextCardNumber.text.toString()
            if (cardNumber.length >= 4) {
                val last4Digits = cardNumber.takeLast(4)
                savedCards.add(last4Digits)
                saveCardsToPreferences()
                dialog.dismiss()
                displaySavedCards()
            } else {
                Toast.makeText(this, "Invalid card number", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    @SuppressLint("StringFormatInvalid")
    private fun displaySavedCards() {
        // Clear previous views
        savedCardsContainer.removeAllViews()

        if (savedCards.isEmpty()) {
            val noCardsView = TextView(this).apply {
                text = getString(R.string.no_saved_cards)
                setTextColor(resources.getColor(R.color.white, null))
            }
            savedCardsContainer.addView(noCardsView)
        } else {
            // Display each card's last 4 digits
            for ((index, card) in savedCards.withIndex()) {
                val cardView = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = LinearLayout.TEXT_ALIGNMENT_CENTER
                }

                val cardTextView = TextView(this).apply {
                    text = getString(R.string.card_ending_in, card)
                    setTextColor(resources.getColor(R.color.white, null))
                    textSize = 16f
                }

                val deleteButton = ImageButton(this).apply {
                    setImageResource(android.R.drawable.ic_delete)
                    setBackgroundColor(resources.getColor(android.R.color.transparent, null))
                    setOnClickListener {
                        confirmDeleteCard(index)
                    }
                }

                cardView.addView(cardTextView)
                cardView.addView(deleteButton)

                savedCardsContainer.addView(cardView)
            }
        }
    }

    private fun confirmDeleteCard(index: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Card")
            .setMessage("Are you sure you want to delete this card?")
            .setPositiveButton("Yes") { _, _ ->
                savedCards.removeAt(index)
                saveCardsToPreferences()
                displaySavedCards()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun saveUserInfo() {
        val name = editTextName.text.toString()
        val phone = editTextPhone.text.toString()
        val email = editTextEmail.text.toString()
        val notes = editTextNotes.text.toString()

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
        val cards = sharedPreferences.getStringSet("cards", setOf()) ?: setOf()
        savedCards.addAll(cards)
        displaySavedCards()
    }

    private fun saveCardsToPreferences() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("cards", savedCards.toSet())
        editor.apply()
    }
}
