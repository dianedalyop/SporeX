package com.example.sporex_app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.WorkManager
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.ExistingPeriodicWorkPolicy
import java.util.concurrent.TimeUnit

class VentilationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        sendNotification()
        return Result.success()
    }

    private fun sendNotification() {
        val channelId = "sporex_alerts"
         val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SporeX Alerts"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = "Urgent ventilation and air quality reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Ventilation Reminder")
            .setContentText("It's time to open your windows for fresh air flow.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

         notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    companion object {
        fun schedule(context: Context, intervalHours: Int) {
             val hours = if (intervalHours <= 0) 1L else intervalHours.toLong()

            val workRequest = PeriodicWorkRequestBuilder<VentilationWorker>(
                hours, TimeUnit.HOURS
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "ventilation_reminder",
                ExistingPeriodicWorkPolicy.UPDATE,   workRequest
            )
        }
    }
}