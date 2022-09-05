package com.app.kera.data.network

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.app.kera.data.models.NotificationModel
import com.app.kera.utils.NotificationUtility.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    //this is called when a message is received
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val notificationModel = NotificationModel()
        //check messages

        Log.d(TAG, "From: ${remoteMessage.from}")
        notificationModel.from = remoteMessage.from


        // Check if message contains a data payload, you can get the payload here and add as an intent to your activity
        remoteMessage.data.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            notificationModel.reportId = remoteMessage.data["reportId"]
            notificationModel.userType = remoteMessage.data["userType"]
            notificationModel.reportType = remoteMessage.data["reportType"]
            notificationModel.type = remoteMessage.data["type"]
            remoteMessage.notification?.let {
                Log.d(TAG, "Message Notification Body: ${it.body}")
                notificationModel.title =  it.title
                notificationModel.body  = it.body
                sendNotification(notificationModel)
            }
        }

        // Check if message contains a notification payload, send notification


    }

    override fun onNewToken(token: String) {

        Log.d("rfst", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)

    }

    private fun sendRegistrationToServer(token: String?) {

        //you can send the updated value of the token to your server here

    }

    private fun sendNotification(notificationModel: NotificationModel){
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        notificationManager.sendNotification(notificationModel, applicationContext)
    }


}