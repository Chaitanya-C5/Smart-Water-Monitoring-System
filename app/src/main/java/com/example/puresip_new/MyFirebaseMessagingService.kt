package com.example.puresip_new

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.internal.notify

const val channelId = "c5"
const val channelName = "CHANNEL 1"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("xcr","yes data")

        val data = remoteMessage.data
        val title = data["title"]
        val body = data["body"]
        val channel_id = data["channel_id"]

        if (title != null && body != null && channel_id != null) {
            generateNotification(title,body,channel_id)
        }
        else {
            Log.d("xcr","No data")
        }
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.example.puresip_new",R.layout.notification)

        remoteView.setTextViewText(R.id.ntitle,title)
        remoteView.setTextViewText(R.id.ndesc,message)
        remoteView.setImageViewResource(R.id.nlogo,R.drawable.applogo)

        return remoteView
    }

    private fun generateNotification(title: String, message: String, channel_id: String)
    {
        val intent = Intent(this,AlertdisplayActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.putExtra("title", title);
        intent.putExtra("channelId", channel_id);
        intent.putExtra("alertMessage", message);

        val pendingIntent = PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            channelId)
            .setSmallIcon(R.drawable.applogo)
            .setAutoCancel(true)
            .setOnlyAlertOnce(false)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())
    }

}