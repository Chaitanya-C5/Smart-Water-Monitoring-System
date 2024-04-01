package com.example.puresip_new

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.puresip_new.databinding.ActivityGlidetestBinding

class GlidetestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGlidetestBinding
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_glidetest)

        val myList = intent.getStringArrayListExtra("urls")

        webView = findViewById(R.id.webView1)

        binding.phBtn.setOnClickListener{
            webView.apply {
                loadUrl(myList!![0])
                settings.javaScriptEnabled=true
            }
        }

        binding.tempBtn.setOnClickListener{
            webView.apply {
                loadUrl(myList!![1])
                settings.javaScriptEnabled=true
            }
        }

        binding.turbBtn.setOnClickListener{
            webView.apply {
                loadUrl(myList!![2])
                settings.javaScriptEnabled=true
            }
        }

        binding.purBtn.setOnClickListener{
            webView.apply {
                loadUrl(myList!![3])
                settings.javaScriptEnabled=true
            }

        }
    }
}
