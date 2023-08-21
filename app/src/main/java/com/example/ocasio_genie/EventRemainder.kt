package com.example.ocasio_genie

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object EventUtils {

    private const val NOTIFICATION_CHANNEL_ID = "event_reminder_channel"
    private const val NOTIFICATION_ID = 1

    fun showEventReminderNotification(context: Context, eventTime: String) {
        if (checkNotificationPermission(context)) {
            val notificationBuilder = buildNotification(context, eventTime)
            showNotification(context, notificationBuilder)
        }
    }

    private fun checkNotificationPermission(context: Context): Boolean {
        val permission = Manifest.permission.VIBRATE
        val result = context.checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid())
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun buildNotification(context: Context, eventTime: String): NotificationCompat.Builder {
        // Build the notification here
        val contentText = "Hurry Up! Your event is starting in an hour ⌛"
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle("Event Reminder")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }

    private fun showNotification(context: Context, notificationBuilder: NotificationCompat.Builder) {
        val notificationManager = NotificationManagerCompat.from(context)
        try {
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        } catch (e: SecurityException) {
            // Handle SecurityException if the permission is not granted
            println("Notifications are disabled!!!")
        }
    }
}
