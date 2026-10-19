package com.group4.medicaladherenceapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.group4.medicaladherenceapp.R

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val medicationName =
            intent.getStringExtra(
                "MEDICATION_NAME"
            ) ?: "Medication"

        val channelId =
            "MEDICATION_REMINDER"

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        val channel =
            NotificationChannel(
                channelId,
                "Medication Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )

        notificationManager
            .createNotificationChannel(
                channel
            )

        val notification =
            NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(
                    R.mipmap.ic_launcher
                )
                .setContentTitle(
                    "Medication Reminder"
                )
                .setContentText(
                    "Time to take $medicationName"
                )
                .setAutoCancel(true)
                .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}