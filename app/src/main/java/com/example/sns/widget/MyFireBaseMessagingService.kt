package com.example.sns.widget

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sns.R
import com.example.sns.room.ChatDataBase
import com.example.sns.room.DataBase
import com.example.sns.view.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.time.LocalDateTime
import java.time.ZoneOffset

class MyFireBaseMessagingService : FirebaseMessagingService() {

    //FCM 토큰발급
    override fun onNewToken(s: String) {
        Log.d("FCM Log", "Refreshed token: $s")
    }

    //메세지 들어온거 받아서 띄움
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            Log.d("FCM Log", "알림 메세지: " + remoteMessage.notification!!.body)
            val messageBody = remoteMessage.notification!!.body
            val messageTitle = remoteMessage.notification!!.title
            DataBase.getInstance(applicationContext)!!.dao().insert(
                ChatDataBase(
                    0, "a", "b", "ASDDF", LocalDateTime.now().toEpochSecond(
                        ZoneOffset.UTC
                    )
                )
            )
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val channelId = "Channel ID"
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelName = "Channel Name"
                val channel =
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(remoteMessage.sentTime.toInt(), notificationBuilder.build())
        }
    }
}