package com.playlab.chatfirebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("LOGGER", "fcm ${message.messageId.toString()}")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}