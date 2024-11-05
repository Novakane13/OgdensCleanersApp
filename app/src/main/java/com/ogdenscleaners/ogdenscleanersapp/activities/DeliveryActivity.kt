package com.ogdenscleaners.ogdenscleanersapp.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityDeliveryBinding
import com.ogdenscleaners.ogdenscleanersapp.models.PickupRequest
import com.ogdenscleaners.ogdenscleanersapp.viewmodel.DeliveryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DeliveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeliveryBinding
    private val deliveryViewModel: DeliveryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set onClickListeners for each button
        binding.buttonTempStopService.setOnClickListener {
            showTempStopServiceDialog()
        }

        binding.buttonCheckAddress.setOnClickListener {
            Log.d("DeliveryActivity", "Check Address Button Clicked")
            val intent = Intent(this, CustomerCheckActivity::class.java)
            startActivity(intent)
        }

        binding.buttonDeliveryInfo.setOnClickListener {
            Log.d("DeliveryActivity", "Delivery Info Button Clicked")
            val intent = Intent(this, DeliveryInfoActivity::class.java)
            startActivity(intent)
        }

        binding.buttonRequestPickup.setOnClickListener {
            showPickupRequestDialog()
        }

        binding.buttonStopDelService.setOnClickListener {
            showStopServiceDialog()
        }

        binding.buttonStartNewService.setOnClickListener {
            Log.d("DeliveryActivity", "Start New Service Button Clicked")
            val intent = Intent(this, StartNewServiceActivity::class.java)
            startActivity(intent)
        }

        // Observe LiveData from ViewModel
        deliveryViewModel.serviceStopDate.observe(this, Observer { date ->
            date?.let {
                Toast.makeText(this, "Delivery service will stop on $it", Toast.LENGTH_LONG).show()
            }
        })

        deliveryViewModel.pickupRequest.observe(this, Observer { request ->
            request?.let {
                Toast.makeText(
                    this,
                    "Pickup scheduled on ${request.date} with note: ${request.note}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            deliveryViewModel.stopService(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTempStopServiceDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Temporarily Stop Service")
        builder.setMessage("Are you sure you want to temporarily stop your delivery service?")
        builder.setPositiveButton("Save") { dialog, _ ->
            deliveryViewModel.tempStopService()
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
                datePicker.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            }, year, month, day)

            datePickerDialog.show()
        }

        builder.setPositiveButton("Save") { dialog, _ ->
            val selectedDate = datePicker.text.toString()
            val note = noteInput.text.toString()
            val pickupRequest = PickupRequest(selectedDate, note)
            deliveryViewModel.requestPickup(pickupRequest)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}
