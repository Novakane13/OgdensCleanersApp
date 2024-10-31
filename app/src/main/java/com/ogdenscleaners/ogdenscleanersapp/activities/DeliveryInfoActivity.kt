package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.R

class DeliveryInfoActivity : AppCompatActivity() {

    private lateinit var addressInput: EditText
    private lateinit var zipCodeInput: EditText
    private lateinit var aptInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_info)

        addressInput = findViewById(R.id.address_input)
        zipCodeInput = findViewById(R.id.zip_code_input)
        aptInput = findViewById(R.id.apt_input)
        notesInput = findViewById(R.id.notes_input)
        saveButton = findViewById(R.id.save_button)

        // Load saved delivery information
        loadDeliveryInformation()

        saveButton.setOnClickListener {
            val address = addressInput.text.toString()
            val zipCode = zipCodeInput.text.toString()
            val apt = aptInput.text.toString()
            val notes = notesInput.text.toString()

            // Save the delivery information
            saveDeliveryInformation(address, zipCode, apt, notes)
        }
    }

    private fun loadDeliveryInformation() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE)
        addressInput.setText(sharedPreferences.getString("address", ""))
        zipCodeInput.setText(sharedPreferences.getString("zip_code", ""))
        aptInput.setText(sharedPreferences.getString("apt", ""))
        notesInput.setText(sharedPreferences.getString("notes", ""))
    }

    private fun saveDeliveryInformation(address: String, zipCode: String, apt: String, notes: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("address", address)
            putString("zip_code", zipCode)
            putString("apt", apt)
            putString("notes", notes)
            apply()
        }
    }
}
