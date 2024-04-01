package com.example.puresip_new

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class notify:Application() {
    final private val CHANNEL_ID = "c5"

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(CHANNEL_ID,
                "Channel 1",NotificationManager.IMPORTANCE_DEFAULT)
            channel.description="This is default notification channel"

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }
}