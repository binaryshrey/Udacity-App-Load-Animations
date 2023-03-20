package com.udacity.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat


val DOWNLOAD_OPTIONS = mapOf(
    "Option1" to mapOf(
        "type" to "Glide - Image Loading Library by Bump Tech",
        "URI" to "https://github.com/bumptech/glide/archive/master.zip"
    ),
    "Option2" to mapOf(
        "type" to "LoadApp - Current repository by Udacity",
        "URI" to "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
    ),
    "Option3" to mapOf(
        "type" to "Retrofit - Type-Safe HTTP client for Android and Java by Square, Inc",
        "URI" to "https://github.com/square/retrofit/archive/master.zip"
    )
)


fun sendNotification(notify: String, currentStatus: String, fileName: String, context: Context) {

    val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
    notificationManager.sendNotification(notify, currentStatus, fileName, context)
}

