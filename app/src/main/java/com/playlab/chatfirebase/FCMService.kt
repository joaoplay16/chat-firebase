package com.playlab.chatfirebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val data: Map<String, String> = message.data

        if (data["sender"] == null) return

        val intent = Intent(this, ChatActivity::class.java)

        FirebaseFirestore.getInstance().collection("/users")
            .document(data["sender"]!!)
            .get()
            .addOnSuccessListener { snapshot ->
                val sender = snapshot.toObject(User::class.java)

                intent.putExtra("user", sender)

                val pIntent = PendingIntent.getActivity(
                    applicationContext, 0, intent, 0
                )

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val notificationChannelId = "my_channed_id_01"

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    val notificationChannel =
                        NotificationChannel(
                            notificationChannelId,
                            "My notifications",
                            NotificationManager.IMPORTANCE_DEFAULT
                        )

                    notificationChannel.description = "Channel description"
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.RED

                    notificationManager.createNotificationChannel(notificationChannel)
                }

                val builder = NotificationCompat.Builder(
                    applicationContext, notificationChannelId
                )

                builder.setAutoCancel(true)
                builder.setSmallIcon(R.mipmap.ic_launcher)
                builder.setContentTitle(data["title"])
                builder.setContentText(data["body"])
                    .setContentIntent(pIntent)

                notificationManager.notify(1, builder.build())
            }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}