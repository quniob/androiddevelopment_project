package com.example.androiddevelopment_project.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.androiddevelopment_project.MainActivity
import com.example.androiddevelopment_project.R

class ClassAlarmReceiver : BroadcastReceiver() {
    
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "class_notification_channel"
        const val NOTIFICATION_ID = 1001
        const val EXTRA_PROFILE_NAME = "extra_profile_name"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        val profileName = intent.getStringExtra(EXTRA_PROFILE_NAME) ?: "Неизвестный пользователь"
        
        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        createNotificationChannel(notificationManager)
        
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Начало пары")
            .setContentText("У $profileName начинается любимая пара!")
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Уведомления о парах",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Уведомления о начале любимой пары"
            enableLights(true)
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
} 