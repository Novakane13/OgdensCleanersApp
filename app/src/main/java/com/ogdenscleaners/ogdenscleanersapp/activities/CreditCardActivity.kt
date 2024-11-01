package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.adapters.CreditCardAdapter
import com.ogdenscleaners.ogdenscleanersapp.models.CreditCard
import org.json.JSONArray
import org.json.JSONObject

class CreditCardActivity : AppCompatActivity() {

    private lateinit var cardNumberInput: EditText
    private lateinit var expiryDateInput: EditText
    private lateinit var cardholderNameInput: EditText
    private lateinit var saveCardButton: Button
    private lateinit var cardsRecyclerView: RecyclerView
    private lateinit var cardAdapter: CreditCardAdapter

    private lateinit var sharedPreferences: SharedPreferences
    private val savedCards: MutableList<CreditCard> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_card)

        // Initialize views
        cardNumberInput = findViewById(R.id.card_number_input)
        expiryDateInput = findViewById(R.id.expiry_date_input)
        cardholderNameInput = findViewById(R.id.cardholder_name_input)
        saveCardButton = findViewById(R.id.save_card_button)
        cardsRecyclerView = findViewById(R.id.cards_recycler_view)

        // Set up SharedPreferences
        sharedPreferences = getSharedPreferences("credit_cards", MODE_PRIVATE)

        // Load saved cards
        loadSavedCards()

        // Set up RecyclerView
        cardAdapter = CreditCardAdapter(savedCards)
        cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        cardsRecyclerView.adapter = cardAdapter

        // Set up save button onClickListener
        saveCardButton.setOnClickListener {
            val cardNumber = cardNumberInput.text.toString()
            val expiryDate = expiryDateInput.text.toString()
            val cardholderName = cardholderNameInput.text.toString()

            if (cardNumber.isNotBlank() && expiryDate.isNotBlank() && cardNumber.length >= 4) {
                val lastFourDigits = cardNumber.takeLast(4)
                val creditCard = CreditCard(
                    cardholderName = cardholderName,
                    lastFourDigits = lastFourDigits,
                    expiryDate = expiryDate
                )
                saveCard(creditCard)
                savedCards.add(creditCard)
                cardAdapter.notifyItemInserted(savedCards.size - 1)

                // Clear inputs
                cardNumberInput.text.clear()
                expiryDateInput.text.clear()
                cardholderNameInput.text.clear()

                Toast.makeText(this, "Card saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid card details.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCard(creditCard: CreditCard) {
        val cardsJson = sharedPreferences.getString("cards", null)
        val jsonArray = if (cardsJson != null) JSONArray(cardsJson) else JSONArray()

        val cardJson = JSONObject().apply {
            put("cardholderName", creditCard.cardholderName)
            put("lastFourDigits", creditCard.lastFourDigits)
            put("expiryDate", creditCard.expiryDate)
        }

        jsonArray.put(cardJson)

        sharedPreferences.edit().putString("cards", jsonArray.toString()).apply()
    }

    private fun loadSavedCards() {
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
    }
}