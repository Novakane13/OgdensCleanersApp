package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.ogdenscleaners.ogdenscleanersapp.R

class CheckDeliveryRangeActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_delivery_range)

        // Setup navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout)
        val logoButton: ImageButton = findViewById(R.id.logoButton)
        logoButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Setup the NavigationView
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation view item clicks here.
            menuItem.isChecked = true
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Setup the Map Fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Setup check delivery button
        val addressInput: TextInputEditText = findViewById(R.id.addressInput)
        val checkButton: Button = findViewById(R.id.checkDeliveryButton)
        checkButton.setOnClickListener {
            val address = addressInput.text.toString()
            if (address.isNotEmpty()) {
                checkIfInDeliveryRange(address)
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set a default location (e.g., center of delivery range)
        val defaultLocation = LatLng(33.8116, -112.1436) // Example: Anthem, Arizona
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
    }

    private fun checkIfInDeliveryRange(address: String) {
        // Placeholder function for checking delivery range
        // In a real implementation, you would geocode the address and compare it against a defined delivery radius
        Toast.makeText(this, "Checking if $address is in delivery range...", Toast.LENGTH_SHORT).show()

        // Example logic (this would need to be replaced with real geocoding and range checking)
        val inRange = address.lowercase().contains("anthem")
        if (inRange) {
            Toast.makeText(this, "Address is within the delivery range!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Address is outside the delivery range.", Toast.LENGTH_LONG).show()
        }
    }
}