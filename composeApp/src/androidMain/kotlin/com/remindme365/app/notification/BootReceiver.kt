package com.remindme365.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.remindme365.app.data.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule all notifications after device restart
            val notificationManager = NotificationManager(context)
            val repository = Repository()
            
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Reschedule event notifications
                    val events = repository.getActiveEvents()
                    events.forEach { event ->
                        notificationManager.scheduleEventNotification(event)
                    }
                    
                    // Reschedule reminder notifications
                    val reminders = repository.getActiveReminders()
                    reminders.forEach { reminder ->
                        notificationManager.scheduleReminderNotification(reminder)
                    }
                } catch (e: Exception) {
                    // Handle error silently for boot receiver
                }
            }
        }
    }
} 