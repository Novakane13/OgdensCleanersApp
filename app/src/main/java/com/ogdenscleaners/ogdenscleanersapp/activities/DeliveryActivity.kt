package com.ogdenscleaners.ogdenscleanersapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.R

class DeliveryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)

        val checkAddressButton = findViewById<Button>(R.id.checkDeliveryButton)

        // Adding navigation from DeliveryActivity to CustomerCheckActivity
        checkAddressButton?.setOnClickListener {
            val intent = Intent(this, CustomerCheckActivity::class.java)
            startActivity(intent)
        }
    }
}
