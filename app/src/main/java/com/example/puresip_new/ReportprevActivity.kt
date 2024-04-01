package com.example.puresip_new

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.puresip_new.databinding.ActivityReportprevBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ReportprevActivity : AppCompatActivity() {
    lateinit var binding: ActivityReportprevBinding
    var unitNo: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reportprev)

        binding.getdata.setOnClickListener {
            unitNo = binding.unitid.text.toString()
            if (unitNo!!.isNotEmpty()) {

                val channelsRef = FirebaseDatabase.getInstance().getReference("channels").child(
                    unitNo!!
                )
                channelsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val urls = ArrayList<String>()
                            dataSnapshot.children.forEach { urlSnapshot ->
                                // Fetch URLs with keys u1, u2, u3, u4
                                if (urlSnapshot.key in listOf("u1", "u2", "u3", "u4")) {
                                    val url = urlSnapshot.getValue(String::class.java)
                                    urls.add(url!!)
                                }
                            }

                            // Pass data to the next activity
                            val intent =
                                Intent(this@ReportprevActivity, GlidetestActivity::class.java)
                            intent.putStringArrayListExtra("urls", urls)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@ReportprevActivity, "Unit number not found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors
                    }
                })

            }
            else {
                Toast.makeText(this, "Enter unit no", Toast.LENGTH_LONG).show()
            }
        }
    }
}

