package com.example.amoz.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.amoz.R
import com.example.amoz.api.managers.FirebaseManager
import com.example.amoz.api.repositories.UserRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@AndroidEntryPoint
class FirebaseMessagingService  : FirebaseMessagingService() {
    @Inject
    lateinit var userRepository: UserRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val title = data["title"]
        val body = data["body"]
        val deeplink: String? = data["deeplink"]
        showNotification(title, body, deeplink)
    }

    override fun onNewToken(token: String) {
        GlobalScope.launch(Dispatchers.IO) {
            userRepository.updatePushToken()
        }
    }

    private fun showNotification(title: String?, body: String?, deeplink: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "default_channel"
        val channelName = "Default Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (!deeplink.isNullOrEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deeplink))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            notificationBuilder.setContentIntent(pendingIntent)
        }

        val notification = notificationBuilder.build()
        notificationManager.notify(UUID.randomUUID().hashCode(), notification)
    }
}