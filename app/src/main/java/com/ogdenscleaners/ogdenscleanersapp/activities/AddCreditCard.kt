package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.R

class AddCreditCardActivity : AppCompatActivity() {
    private lateinit var editTextCardNumber: EditText
    private lateinit var editTextCardExpiry: EditText
    private lateinit var editTextCardCVV: EditText
    private lateinit var buttonSaveCard: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_credit_card)

        editTextCardNumber = findViewById(R.id.editTextCardNumber)
        editTextCardExpiry = findViewById(R.id.editTextCardExpiry)
        editTextCardCVV = findViewById(R.id.editTextCardCVV)
        buttonSaveCard = findViewById(R.id.buttonSaveCard)

        buttonSaveCard.setOnClickListener {
            val cardNumber = editTextCardNumber.text.toString()
            val cardExpiry = editTextCardExpiry.text.toString()
            val cardCVV = editTextCardCVV.text.toString()

            saveCreditCard(cardNumber, cardExpiry, cardCVV)
        }
    }

    private fun saveCreditCard(number: String, expiry: String, cvv: String) {
        // TODO: Save credit card info securely (e.g., only last 4 digits)
    }
}
