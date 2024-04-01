package com.example.puresip_new

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AlertActivity : AppCompatActivity(), CustomAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("alerts")

        // Create an empty list to hold the data retrieved from Firebase
        val dataList = ArrayList<RecyclerModel>()

        // Create an instance of the CustomAdapter
        adapter = CustomAdapter(dataList, this)

        // Set the adapter to the RecyclerView
        recyclerView.adapter = adapter

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val date = snapshot.child("date").getValue(String::class.java) ?: ""
                    val alertMessage = snapshot.child("alertMessage").getValue(String::class.java) ?: ""
                    val title = snapshot.child("title").getValue(String::class.java) ?: ""
                    val latitude = snapshot.child("latitude").getValue(Double::class.java) ?: 0.0
                    val longitude = snapshot.child("longitude").getValue(Double::class.java) ?: 0.0

                    val item = RecyclerModel(date, alertMessage, title, latitude, longitude)
                    dataList.add(item)
                }
                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    override fun onItemClick(item: RecyclerModel) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("clickedItem", item)
        startActivity(intent)
    }
}


