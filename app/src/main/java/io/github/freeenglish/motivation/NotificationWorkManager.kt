package io.github.freeenglish.motivation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Worker
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.WorkerParameters
import io.github.freeenglish.R
import android.app.PendingIntent
import android.content.Intent
import io.github.freeenglish.MainActivity


class NotificationWorkManager(
    private val context: Context, params: WorkerParameters
) : Worker(context, params) {

    companion object {
        private const val NOTIFY_ID = 1
        private const val CHANNEL_ID = "io.github.freeenglish.motivation"
    }

    override fun doWork(): Result {
        val intent = Intent(applicationContext,MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("Have you already given up?")
                .setContentText("Hey, don't forget pass your test today")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SomeChannel"
            val descriptionText = "SomeDescriptionText"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val channelManager: NotificationManager =
                getSystemService(context, NotificationManager::class.java) as NotificationManager
            channelManager.createNotificationChannel(channel)
       }
        val notificationManager =
            NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(NOTIFY_ID, builder.build())

        return Result.success()
    }
}