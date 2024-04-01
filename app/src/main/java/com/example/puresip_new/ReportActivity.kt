package com.example.puresip_new

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.puresip_new.databinding.ActivityReportBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

class ReportActivity : AppCompatActivity() {
    lateinit var dref: DatabaseReference
    lateinit var binding: ActivityReportBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_report)
        dref = FirebaseDatabase.getInstance().reference
        val query = dref.orderByChild("Date").startAt("").endAt("")

        val sd = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        binding.gDate.setText(sd.format(Date()))
        //binding.gDate.setText("17-12-23")

        val st = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        binding.gTime.setText(st.format(Date()))

        binding.analyze.setOnClickListener {

            var temp = binding.temp.text.toString()
            var ph = binding.ph.text.toString()
            var turb = binding.turbidity.text.toString()

            var purity = binding.purity.text.toString()
            var unitNo = binding.uno.text.toString()

            var loc = "10,12"



//            val dataclass = Dataclass(binding.gDate.text.toString(),binding.gTime.text.toString(),temp,ph,turb,purity,loc,unitNo)
//
//            dref.child(dref.push().key.toString()).setValue(dataclass)

            Toast.makeText(this,"Inserted",Toast.LENGTH_LONG).show()
        }
    }
}