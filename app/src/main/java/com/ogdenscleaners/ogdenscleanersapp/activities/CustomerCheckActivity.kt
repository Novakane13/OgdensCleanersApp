package com.ogdenscleaners.ogdenscleanersapp.activities

import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import com.ogdenscleaners.ogdenscleanersapp.R
import java.util.Locale

class CustomerCheckActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var customerAddressInput: EditText
    private lateinit var checkButton: Button
    private lateinit var deliveryCenter: LatLng
    private val deliveryRadiusMeters = 16093.4 // 10 miles in meters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_delivery_range)

        customerAddressInput = findViewById(R.id.addressInput)
        checkButton = findViewById(R.id.checkDeliveryButton)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val geocoder = Geocoder(this, Locale.getDefault())
        val addressList = geocoder.getFromLocationName("3655 W. Anthem Way, New River, AZ 85086", 1)
        deliveryCenter = if (!addressList.isNullOrEmpty()) {
            val address = addressList[0]
            LatLng(address.latitude, address.longitude)
        } else {
            LatLng(33.8657, -112.1420) // Default location if address lookup fails
        }

        checkButton.setOnClickListener {
            val customerAddress = customerAddressInput.text.toString()
            val customerLatLng = getLocationFromAddress(customerAddress)

            if (customerLatLng != null) {
                val distance = SphericalUtil.computeDistanceBetween(customerLatLng, deliveryCenter)
                if (distance <= deliveryRadiusMeters) {
                    Toast.makeText(this, "Within delivery range!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Not within delivery range.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.addMarker(MarkerOptions().position(deliveryCenter).title("Center of Delivery Area"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deliveryCenter, 12f))

        val deliveryCircle = CircleOptions()
            .center(deliveryCenter)
            .radius(deliveryRadiusMeters)
            .strokeColor(0xFF6200EE.toInt())
            .fillColor(0x556200EE.toInt())

        mMap.addCircle(deliveryCircle)
    }

    private fun getLocationFromAddress(address: String): LatLng? {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addressList = geocoder.getFromLocationName(address, 1)
            if (!addressList.isNullOrEmpty()) {
                val location = addressList[0]
                LatLng(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
