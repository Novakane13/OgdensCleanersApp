package com.ogdenscleaners.ogdenscleanersapp.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityDeliveryBinding
import com.ogdenscleaners.ogdenscleanersapp.models.DeliveryRequest
import com.ogdenscleaners.ogdenscleanersapp.viewmodel.DeliveryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DeliveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeliveryBinding
    private val deliveryViewModel: DeliveryViewModel by viewModels()
    private var selectedDate: String = ""
    private var instructionsinput: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
        observeViewModel()
    }

    private fun setupButtons() {
        binding.requestapickup.setOnClickListener { showPickupRequestDialog() }
    }

    @SuppressLint("InflateParams")
    private fun showPickupRequestDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.pickup_request_dialog, null)
        val selectDateButton = dialogView.findViewById<Button>(R.id.pickupdate)
        val instructionsButton = dialogView.findViewById<Button>(R.id.pickupinstructions)
        val saveRequestButton = dialogView.findViewById<Button>(R.id.saverequest)

        builder.setView(dialogView)
        val dialog = builder.create()

        selectDateButton.setOnClickListener { showDatePickerDialog { date -> selectedDate = date } }
        instructionsButton.setOnClickListener { showPickupInstructionsDialog { instructions -> instructionsinput = instructions } }

        saveRequestButton.setOnClickListener {
            if (selectedDate.isEmpty() || instructionsinput.isEmpty()) {
                Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show()
            } else {
                val pickupRequest = DeliveryRequest(
                    customerId = "12345", // Replace with dynamic ID
                    address = "Customer Address", // Replace with dynamic address
                    date = selectedDate,
                    instructions = instructionsinput
                )
                deliveryViewModel.requestPickup(pickupRequest)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year"
                onDateSelected(date)
                Toast.makeText(this, "Date Selected: $date", Toast.LENGTH_SHORT).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showPickupInstructionsDialog(onInstructionsSaved: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.pickup_instructions, null)
        val saveButton = dialogView.findViewById<Button>(R.id.saverequest)
        val instructionsInput = dialogView.findViewById<EditText>(R.id.instructionsinput)

        builder.setView(dialogView)
        val dialog = builder.create()

        saveButton.setOnClickListener {
            val instructions = instructionsInput.text.toString().trim()
            if (instructions.isNotEmpty()) {
                onInstructionsSaved(instructions)
                dialog.dismiss()
                Toast.makeText(this, "Instructions Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid instructions.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun observeViewModel() {
        deliveryViewModel.pickupRequest.observe(this) { request ->
            request?.let {
                Toast.makeText(this, "Pickup Scheduled: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
