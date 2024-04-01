package com.example.puresip_new

import android.os.Bundle
import android.os.Parcelable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.puresip_new.RecyclerModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var clickedItem: RecyclerModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        clickedItem = intent.getParcelableExtra<RecyclerModel>("clickedItem") ?: return

        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val alertMessageTextView: TextView = findViewById(R.id.alertMessageTextView)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val latitudeTextView: TextView = findViewById(R.id.latitudeTextView)
        val longitudeTextView: TextView = findViewById(R.id.longitudeTextView)

        dateTextView.text = clickedItem.date
        alertMessageTextView.text = clickedItem.alertMessage
        titleTextView.text = clickedItem.title
        latitudeTextView.text = clickedItem.latitude.toString()
        longitudeTextView.text = clickedItem.longitude.toString()

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.dmapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        val location = LatLng(clickedItem.latitude, clickedItem.longitude)
        mGoogleMap.addMarker(MarkerOptions().position(location).title("Alert Location"))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}
