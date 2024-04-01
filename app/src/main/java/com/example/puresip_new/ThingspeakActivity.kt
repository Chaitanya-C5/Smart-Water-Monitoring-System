package com.example.puresip_new

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.puresip_new.databinding.ActivityThingspeakBinding
import org.json.JSONObject


class ThingspeakActivity : AppCompatActivity(), ThingSpeakDataFetcher.OnDataFetchedListener {
        lateinit var binding:ActivityThingspeakBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this,R.layout.activity_thingspeak)

            fetchThingSpeakData()
        }

        private fun fetchThingSpeakData() {
            val thingSpeakDataFetcher = ThingSpeakDataFetcher(this)
            thingSpeakDataFetcher.execute()
        }

        override fun onDataFetched(data: String?) {
            // Parse JSON response and update UI
            if (data != null) {
                // Parse JSON here and update UI
                val jsonObject = JSONObject(data)
                val fieldValue = jsonObject.optString("field1")
                binding.td.text = jsonObject.toString()
            }
        }
    }
