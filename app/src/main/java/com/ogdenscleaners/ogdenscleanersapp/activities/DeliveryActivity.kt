package com.ogdenscleaners.ogdenscleanersapp.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.R
import java.util.Calendar

class DeliveryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)

        // Reference all the buttons
        val tempStopServiceButton = findViewById<Button>(R.id.button)
        val checkAddressButton = findViewById<Button>(R.id.addresscheck)
        val deliveryInfoButton = findViewById<Button>(R.id.updateorview)
        val requestPickupButton = findViewById<Button>(R.id.requestapickup)
        val stopDelServiceButton = findViewById<Button>(R.id.stopdelservice)
        val startNewServiceButton = findViewById<Button>(R.id.startnew)

        // Set onClickListeners for each button
        tempStopServiceButton.setOnClickListener {
            showTempStopServiceDialog()
        }

        checkAddressButton.setOnClickListener {
            Log.d("DeliveryActivity", "Check Address Button Clicked")
            val intent = Intent(this, CustomerCheckActivity::class.java)
            startActivity(intent)
        }

        deliveryInfoButton.setOnClickListener {
            Log.d("DeliveryActivity", "Delivery Info Button Clicked")
            val intent = Intent(this, DeliveryInfoActivity::class.java)
            startActivity(intent)
        }

        requestPickupButton.setOnClickListener {
            showPickupRequestDialog()
        }

        stopDelServiceButton.setOnClickListener {
            showStopServiceDialog()
        }

        startNewServiceButton.setOnClickListener {
            Log.d("DeliveryActivity", "Start New Service Button Clicked")
            val intent = Intent(this, StartNewServiceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showStopServiceDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Stop Service")
        builder.setMessage("Are you sure you want to stop your delivery service?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            showDatePickerDialog()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            Toast.makeText(this, "Delivery service will stop on $selectedDay/${selectedMonth + 1}/$selectedYear", Toast.LENGTH_LONG).show()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTempStopServiceDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Temporarily Stop Service")
        builder.setMessage("Are you sure you want to temporarily stop your delivery service?")
        builder.setPositiveButton("Save") { dialog, _ ->
            Toast.makeText(this, "Temporary service stop saved.", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showPickupRequestDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Request Pickup")

        val pickupLayout = layoutInflater.inflate(R.layout.pickup_request_dialog, null)
        builder.setView(pickupLayout)

        val datePicker = pickupLayout.findViewById<EditText>(R.id.pickup_date)
        val noteInput = pickupLayout.findViewById<EditText>(R.id.pickup_note)

        datePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                datePicker.setText(getString(R.string.date_format, selectedDay, selectedMonth + 1, selectedYear))
            }, year, month, day)

            datePickerDialog.show()
        }

        builder.setPositiveButton("Save") { dialog, _ ->
            val selectedDate = datePicker.text.toString()
            val note = noteInput.text.toString()
            Toast.makeText(this, "Pickup scheduled on $selectedDate with note: $note", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}
