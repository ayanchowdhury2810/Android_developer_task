package com.android.androiddevelopertask.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class TaskNotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val taskId = inputData.getString("taskId") ?: return Result.failure()
        val taskTitle = inputData.getString("taskTitle") ?: "Task Reminder"
        Log.d("AAAAAAA", "eeeeeeeeee")

        sendNotification(taskId, taskTitle)
        return Result.success()
    }

    private fun sendNotification(taskId: String, taskTitle: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "task_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("AAAAAAA", "111111111")
            val channel = NotificationChannel(
                channelId,
                "Task Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }


        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Task Reminder")
            .setContentText(taskTitle)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        Log.d("AAAAAAA", "222222")

        notificationManager.notify(taskId.hashCode(), notification)
    }
}
