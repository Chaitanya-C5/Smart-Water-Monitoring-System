package com.example.puresip_new

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.puresip_new.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard)
        binding.alert.setOnClickListener{
            startActivity(Intent(this,AlertActivity::class.java))
        }

        binding.contact.setOnClickListener{
            startActivity(Intent(this,contactActivity::class.java))
        }

        binding.logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        binding.report.setOnClickListener {
            startActivity(Intent(this,ReportprevActivity::class.java))
        }

        binding.about.setOnClickListener {
            startActivity(Intent(this,AboutActivity::class.java))
        }
        getFCMToken();
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                //FirebaseUtil.currentUserDetails().update("fcmToken", token)
                Log.i("mytoken",token);
            }
        }
    }



}