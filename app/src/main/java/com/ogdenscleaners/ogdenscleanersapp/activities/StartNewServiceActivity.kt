package com.ogdenscleaners.ogdenscleanersapp.activities

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class StartNewServiceActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var deliveryAddressInput: EditText
    private lateinit var firstDeliveryDateInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_new_service)

        // Initialize views
        firstDeliveryDateInput = findViewById(R.id.firstdeliverydate)
        nameInput = findViewById(R.id.startservicename)
        phoneInput = findViewById(R.id.startservicephone)
        deliveryAddressInput = findViewById(R.id.startserviceaddress)
        notesInput = findViewById(R.id.startservicenotes)
        saveButton = findViewById(R.id.save_button)

        // Set click listener for the delivery date input to show a DatePicker
        firstDeliveryDateInput.setOnClickListener {
            showDatePicker()
        }

        // Set click listener for the save button
        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val deliveryAddress = deliveryAddressInput.text.toString().trim()
            val firstDeliveryDate = firstDeliveryDateInput.text.toString().trim()
            val notes = notesInput.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || deliveryAddress.isEmpty() || firstDeliveryDate.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            } else {
                saveDeliveryInformation(name, phone, deliveryAddress, firstDeliveryDate, notes)
                Toast.makeText(this, "Service information saved successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to show the DatePicker for selecting the first delivery date
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                firstDeliveryDateInput.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    // Method to save the entered information into SharedPreferences
    private fun saveDeliveryInformation(
        name: String,
        phone: String,
        address: String,
        firstDeliveryDate: String,
        notes: String
    ) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("name", name)
            putString("phone", phone)
            putString("address", address)
            putString("first_delivery_date", firstDeliveryDate)
            putString("notes", notes)
            apply()
        }
    }
}
