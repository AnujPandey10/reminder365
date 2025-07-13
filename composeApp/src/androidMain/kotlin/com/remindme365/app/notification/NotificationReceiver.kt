package com.remindme365.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notification_id", 0)
        val title = intent.getStringExtra("title") ?: "RemindMe365"
        val message = intent.getStringExtra("message") ?: "You have a reminder!"
        
        val notificationManager = NotificationManager(context)
        notificationManager.showNotification(title, message, notificationId)
    }
} 