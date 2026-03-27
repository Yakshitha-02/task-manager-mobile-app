package com.example.taskmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {

        val channelId = "task_channel"

        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // create channel (required for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Task Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        // build notification
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Task Reminder ⏰")
            .setContentText("Don't forget to complete your tasks!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        manager.notify(1, notification)

        return Result.success()
    }
}