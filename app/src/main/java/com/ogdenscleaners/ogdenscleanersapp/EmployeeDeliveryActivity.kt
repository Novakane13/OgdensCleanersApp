package com.ogdenscleaners.ogdenscleanersapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class EmployeeDeliveryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_delivery)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val origin = LatLng(33.45, -112.07) // Example starting point
        val destination = LatLng(33.46, -112.05) // Example destination

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 10f))

        // Get optimized route
        getOptimizedRoute(origin, destination)
    }

    private fun getOptimizedRoute(origin: LatLng, destination: LatLng) {
        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${destination.latitude},${destination.longitude}&waypoints=optimize:true|33.44,-112.08|33.45,-112.09&key=AIzaSyA9_HFfk9t48l4_G0mCuIZss6PjSqN9jrQ"

        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(this@EmployeeDeliveryActivity, "Failed to fetch route", Toast.LENGTH_SHORT).show() }
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body()?.string()?.let { JSONObject(it) }
                val routes = jsonResponse?.getJSONArray("routes")
                val points = routes?.getJSONObject(0)?.getJSONObject("overview_polyline")?.getString("points")

                val polylineOptions = PolylineOptions().addAll(PolyUtil.decode(points))
                runOnUiThread { mMap.addPolyline(polylineOptions) }
            }
        })
    }
}
