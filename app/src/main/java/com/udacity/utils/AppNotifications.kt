package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.MainActivity
import com.udacity.R

private const val NOTIFICATION_ID = 10101
private const val NOTIFICATION_CHANNEL_ID = "Udacity Loading Channel"
private const val NOTIFICATION_TITLE = "Udacity: Android Kotlin Nanodegree Project 3"


//extension function to build and send notifications
fun NotificationManager.sendNotification(messageBody: String, status: String, fileName: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    // Create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    // Building notification
    val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(messageBody)
        .setSmallIcon(R.drawable.vector_cloud_image)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(0, "Check Status", contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Calling notify
    notify(NOTIFICATION_ID, builder.build())
}

//cancelling all registered notifications if any
fun NotificationManager.cancelNotifications() {
    cancelAll()
}
