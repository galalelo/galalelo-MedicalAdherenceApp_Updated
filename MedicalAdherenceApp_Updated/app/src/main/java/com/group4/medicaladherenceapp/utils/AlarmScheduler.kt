package com.group4.medicaladherenceapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class AlarmScheduler(
    private val context: Context
) {

    fun scheduleMedicationReminder(
        medicationName: String,
        hour: Int,
        minute: Int
    ) {

        val intent = Intent(
            context,
            NotificationReceiver::class.java
        )

        intent.putExtra(
            "MEDICATION_NAME",
            medicationName
        )

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                medicationName.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val calendar =
            Calendar.getInstance()

        calendar.set(
            Calendar.HOUR_OF_DAY,
            hour
        )

        calendar.set(
            Calendar.MINUTE,
            minute
        )

        calendar.set(
            Calendar.SECOND,
            0
        )

        calendar.set(
            Calendar.MILLISECOND,
            0
        )

        // If selected time has already passed today,
        // schedule it for tomorrow.
        if (
            calendar.timeInMillis <=
            System.currentTimeMillis()
        ) {

            calendar.add(
                Calendar.DAY_OF_MONTH,
                1
            )
        }

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}