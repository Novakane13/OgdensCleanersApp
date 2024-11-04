package com.ogdenscleaners.ogdenscleanersapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.adapters.CreditCardAdapter
import com.ogdenscleaners.ogdenscleanersapp.models.Customer.CreditCard
import com.ogdenscleaners.ogdenscleanersapp.repositories.CustomerRepository
import com.ogdenscleaners.ogdenscleanersapp.services.PaymentHelper
import com.stripe.android.model.CardParams
import org.json.JSONArray

class CreditCardActivity : AppCompatActivity() {

    private lateinit var cardNumberInput: EditText
    private lateinit var expiryDateInput: EditText
    private lateinit var cardholderNameInput: EditText
    private lateinit var saveCardButton: Button
    private lateinit var cardsRecyclerView: RecyclerView
    private lateinit var cardAdapter: CreditCardAdapter

    private val savedCards: MutableList<CreditCard> = mutableListOf()
    private lateinit var paymentHelper: PaymentHelper
    private lateinit var customerRepository: CustomerRepository
    private val customerId = "customerId_here" // Replace with actual customer ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_card)

        // Initialize views
        cardNumberInput = findViewById(R.id.card_number_input)
        expiryDateInput = findViewById(R.id.expiry_date_input)
        cardholderNameInput = findViewById(R.id.cardholder_name_input)
        saveCardButton = findViewById(R.id.save_card_button)
        cardsRecyclerView = findViewById(R.id.cards_recycler_view)

        // Initialize PaymentHelper and CustomerRepository
        paymentHelper = PaymentHelper(applicationContext, "your_publishable_key_here")
        customerRepository = CustomerRepository()

        // Set up RecyclerView
        cardAdapter = CreditCardAdapter(savedCards)
        cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        cardsRecyclerView.adapter = cardAdapter

        // Load saved cards from SharedPreferences
        loadSavedCreditCards()

        // Set up save button onClickListener
        saveCardButton.setOnClickListener {
            val cardNumber = cardNumberInput.text.toString()
            val expiryDate = expiryDateInput.text.toString()
            val cardholderName = cardholderNameInput.text.toString()

            if (cardNumber.isNotBlank() && expiryDate.isNotBlank() && cardNumber.length >= 4) {
                val expDateParts = expiryDate.split("/")
                if (expDateParts.size == 2) {
                    val expMonth = expDateParts[0].toIntOrNull() ?: return@setOnClickListener
                    val expYear = expDateParts[1].toIntOrNull() ?: return@setOnClickListener
                    val cardParams = CardParams(
                        number = cardNumber,
                        expMonth = expMonth,
                        expYear = expYear,
                        cvc = "123" // Replace with actual CVC input from user
                    )

                    // Tokenize the card using PaymentHelper
                    paymentHelper.tokenizeCard(cardParams) { result ->
                        result.onSuccess { token ->
                            val lastFourDigits = cardNumber.takeLast(4)
                            val creditCard = CreditCard(
                                cardholderName = cardholderName,
                                lastFourDigits = lastFourDigits,
                                expirationDate = expiryDate,
                                cardToken = token.id
                            )

                            // Save card to Firebase or wherever necessary
                            customerRepository.addCreditCardToCustomer(customerId, creditCard)
                            savedCards.add(creditCard)
                            cardAdapter.notifyItemInserted(savedCards.size - 1)

                            // Clear inputs
                            cardNumberInput.text.clear()
                            expiryDateInput.text.clear()
                            cardholderNameInput.text.clear()

                            Toast.makeText(this, "Card saved successfully!", Toast.LENGTH_SHORT).show()
                        }.onFailure { error ->
                            Toast.makeText(this, "Error: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Please enter a valid expiry date in MM/YY format.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter valid card details.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadSavedCreditCards() {
        savedCards.clear()
        val cardsJson = getSharedPreferences("credit_cards", MODE_PRIVATE).getString("cards", null)
        if (cardsJson != null) {
            val jsonArray = JSONArray(cardsJson)
            for (i in 0 until jsonArray.length()) {
                val cardJson = jsonArray.getJSONObject(i)
                val card = CreditCard(
                    cardholderName = cardJson.getString("cardholderName"),
                    lastFourDigits = cardJson.getString("lastFourDigits"),
                    expirationDate = cardJson.getString("expiryDate"),
                    cardToken = cardJson.optString("cardToken")
                )
                savedCards.add(card)
            }
            cardAdapter.notifyDataSetChanged()
        }
    }
}
