package com.example.puresip_new

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.puresip_new.databinding.ActivityAlertdisplayBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AlertdisplayActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityAlertdisplayBinding
    private var mGoogleMap: GoogleMap? = null
    private val database = FirebaseDatabase.getInstance()
    private val alertsRef = database.getReference("alerts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alertdisplay)
        Log.d("abc", "Activity created")
        val title = intent.getStringExtra("title")
        val channelId = intent.getStringExtra("channelId")
        val alertMessage = intent.getStringExtra("alertMessage")

        if (title != null && channelId != null && alertMessage != null) {
            Log.d("abc", "$title $channelId $alertMessage")

            binding.atitle.text = title

            val calendar: Calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val currentDate: String = dateFormat.format(calendar.time)

            binding.adate.text = currentDate
            binding.aalert.text = alertMessage

            fetchLocationData(channelId)
        }
    }

    private fun fetchLocationData(channelId: String) {
        val client = OkHttpClient()

        // Fetch latitude data
        val latitudeRequest = Request.Builder()
            .url("https://api.thingspeak.com/channels/2413615/status.json")
            .build()

        client.newCall(latitudeRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val latitude = parseLatitude(json)
                runOnUiThread {
                    binding.lat.text = latitude
                    initializeMapIfNeeded()
                }
            }
        })

        // Fetch longitude data
        val longitudeRequest = Request.Builder()
            .url("https://api.thingspeak.com/channels/2413615/status.json")
            .build()

        client.newCall(longitudeRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val longitude = parseLongitude(json)
                runOnUiThread {
                    binding.longit.text = longitude
                    initializeMapIfNeeded()
                    // Insert data into database once longitude is fetched
                    insertDataIntoDatabase()
                }
            }
        })
    }

    private fun initializeMapIfNeeded() {
        val latitude = binding.lat.text.toString().toDoubleOrNull()
        val longitude = binding.longit.text.toString().toDoubleOrNull()

        if (latitude != null && longitude != null) {
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0

        val latitude = binding.lat.text.toString().toDoubleOrNull()
        val longitude = binding.longit.text.toString().toDoubleOrNull()

        if (latitude != null && longitude != null) {
            val location = LatLng(latitude, longitude)
            mGoogleMap?.addMarker(MarkerOptions().position(location).title("Alert Location"))
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        } else {
            Log.e("xyz", "Invalid latitude or longitude $latitude and $longitude")
        }
    }

    fun parseLatitude(jsonString: String?): String {
        try {
            val jsonObject = JSONObject(jsonString)
            val channelObject = jsonObject.getJSONObject("channel")
            val latitude = channelObject.getString("latitude")
            return latitude
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return "Latitude not available"
    }

    private fun parseLongitude(jsonString: String?): String {
        try {
            val jsonObject = JSONObject(jsonString)
            val channelObject = jsonObject.getJSONObject("channel")
            val longitude = channelObject.getString("longitude")
            return longitude
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return "Longitude not available"
    }

    private fun insertDataIntoDatabase() {
        val title = binding.atitle.text.toString()
        val date = binding.adate.text.toString()
        val alertMessage = binding.aalert.text.toString()
        val latitude = binding.lat.text.toString().toDoubleOrNull() ?: 0.0
        val longitude = binding.longit.text.toString().toDoubleOrNull() ?: 0.0

        val alertData = Alert(title, date, alertMessage, latitude, longitude)
        val alertKey = alertsRef.push().key
        alertKey?.let {
            alertsRef.child(it).setValue(alertData)
                .addOnSuccessListener {
                    Log.d("Insertion", "Data inserted successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("Insertion", "Error inserting data", e)
                }
        }
    }
    data class Alert(
        val title: String = "",
        val date: String = "",
        val alertMessage: String = "",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
    )
}
