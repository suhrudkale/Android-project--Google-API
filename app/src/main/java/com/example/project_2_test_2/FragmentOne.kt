package com.example.project_2_test_2

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Call
import okhttp3.Response
import com.google.gson.Gson
import java.io.IOException

class FragmentOne : Fragment(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_one, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyAUOdeM84gVpDC-qDaaH78e9hCqYmmEdFQ")
        }
        placesClient = Places.createClient(requireContext())

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        updateLocationUI()
    }

    private fun updateLocationUI() {
        try {
            if (hasLocationPermission()) {
                googleMap?.isMyLocationEnabled = true
                googleMap?.uiSettings?.isMyLocationButtonEnabled = true
                getNearestMall()
            } else {
                requestLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception", "Security Exception: Permission not granted")
        }
    }

    private fun hasLocationPermission() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun getNearestMall() {
        try {
            if (hasLocationPermission()) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        findNearestMall(it.latitude, it.longitude)
                    } ?: Log.w("Location", "Location is null")
                }.addOnFailureListener {
                    Log.w("Location", "Failed to get location", it)
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception", "Security Exception: Permission not granted")
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun findNearestMall(latitude: Double, longitude: Double) {
        val apiKey = "AIzaSyAUOdeM84gVpDC-qDaaH78e9hCqYmmEdFQ" // Replace with your API key
        val latitude = "42.3508"

        val types = "supermarket|department_store|clothing_store|electronics_store"
        val longitude = "-71.1089"

        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latitude,$longitude&radius=1200&type=$types&key=$apiKey"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                Log.e("HTTPError", "Failed to fetch data: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    activity?.runOnUiThread {
                        if (responseData != null) {
                            val gson = Gson()
                            val placesResponse = gson.fromJson(responseData, PlacesResponse::class.java)
                            displayNearestMall(placesResponse.results, latitude, longitude)
                        }
                    }
                }
            }
        })
    }

    private fun displayNearestMall(places: List<Result>, currentLatitude: String, currentLongitude: String) {
        if (places.isNotEmpty()) {
            val sortedPlaces = places.sortedWith(compareBy { distance(currentLatitude, currentLongitude, it.geometry.location.lat, it.geometry.location.lng) })

            for (place in sortedPlaces) {
                Log.d("Place", "Name: ${place.name}, Distance: ${distance(currentLatitude, currentLongitude, place.geometry.location.lat, place.geometry.location.lng)}")
            }

            // Displaying the name of the nearest place in your TextView
            val nearestMall = sortedPlaces[0].name
            view?.findViewById<TextView>(R.id.textViewYourMall)?.text = nearestMall
        } else {
            Log.d("Place", "No places found")
        }
    }


    private fun distance(lat1: String, lon1: String, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1.toDouble(), lon1.toDouble(), lat2, lon2, results)
        return results[0]
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    updateLocationUI()
                } else {
                    // Permission was denied. Disable the functionality that depends on this permission.
                    // You can also alert the user that the permission is necessary for the feature to function properly.
                    Toast.makeText(context, "Location permission is needed to display nearest mall", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}

