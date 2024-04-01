package com.example.puresip_new

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.puresip_new.databinding.ActivityLoginBinding
import com.example.puresip_new.databinding.ActivitySignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        binding.signup.setOnClickListener {
            val suname = binding.suname.text.toString()
            val supwd = binding.supwd.text.toString()
            val csupwd = binding.scupwd.text.toString()

            if (suname.isEmpty() || supwd.isEmpty() || csupwd.isEmpty()) {
                Toast.makeText(this, "Please enter all credentials", Toast.LENGTH_LONG)
                    .show()
            } else if (supwd.length < 6) {
                Toast.makeText(
                    this,
                    "Password length should be minimum 6 characters",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else if (supwd != csupwd) {
                Toast.makeText(
                    this,
                    "Confirm password and password not matching",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                auth.createUserWithEmailAndPassword(suname, supwd)
                    .addOnCompleteListener(OnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "User created successfully", Toast.LENGTH_LONG)
                                .show()
                            startActivity(Intent(this, DashboardActivity::class.java))
                            finish()
                        }
                    })
            }

        }

        binding.ulog.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}