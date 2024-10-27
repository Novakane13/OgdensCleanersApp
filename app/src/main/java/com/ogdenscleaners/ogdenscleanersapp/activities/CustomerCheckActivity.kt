package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.PolyUtil
import com.ogdenscleaners.ogdenscleanersapp.R

class CustomerCheckActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var customerAddressInput: EditText
    private lateinit var checkButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_delivery_range)

        customerAddressInput = findViewById(R.id.addressInput)
        checkButton = findViewById(R.id.checkDeliveryButton)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkButton.setOnClickListener {
            val address = customerAddressInput.text.toString()
            // Placeholder coordinates for the customer's address
            val customerLocation = LatLng(33.45, -112.08) // Example coordinates for testing

            // Define the delivery area - Update these to match your actual area
            val deliveryArea = PolygonOptions()
                .add(LatLng(33.45, -112.07), LatLng(33.46, -112.05), LatLng(33.44, -112.09))

            // Check if the customer location is within the delivery area
            val isWithinDelivery = PolyUtil.containsLocation(customerLocation, deliveryArea.points, true)

            if (isWithinDelivery) {
                Toast.makeText(this, "Within delivery range!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Not within delivery range.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val centerPoint = LatLng(33.45, -112.07) // Center of your delivery area
        mMap.addMarker(MarkerOptions().position(centerPoint).title("Center of Delivery Area"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, 12f))

        // Draw the delivery area on the map
        val deliveryArea = PolygonOptions()
            .add(LatLng(33.45, -112.07), LatLng(33.46, -112.05), LatLng(33.44, -112.09))
            .strokeColor(0xFF6200EE.toInt())
            .fillColor(0x556200EE.toInt()) // Semi-transparent fill

        mMap.addPolygon(deliveryArea)
    }
}
