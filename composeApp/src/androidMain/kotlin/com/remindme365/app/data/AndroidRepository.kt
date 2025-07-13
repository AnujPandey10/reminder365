package com.remindme365.app.data

import com.remindme365.app.notification.NotificationManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AndroidRepository : Repository(), KoinComponent {
    
    private val notificationManager: NotificationManager by inject()
    
    override suspend fun saveEvent(event: Event): Event {
        val savedEvent = super.saveEvent(event)
        notificationManager.scheduleEventNotification(savedEvent)
        return savedEvent
    }
    
    override suspend fun updateEvent(event: Event) {
        super.updateEvent(event)
        notificationManager.scheduleEventNotification(event)
    }
    
    override suspend fun deleteEvent(id: String) {
        notificationManager.cancelEventNotification(id)
        super.deleteEvent(id)
    }
    
    override suspend fun saveReminder(reminder: Reminder): Reminder {
        val savedReminder = super.saveReminder(reminder)
        notificationManager.scheduleReminderNotification(savedReminder)
        return savedReminder
    }
    
    override suspend fun updateReminder(reminder: Reminder) {
        super.updateReminder(reminder)
        notificationManager.scheduleReminderNotification(reminder)
    }
    
    override suspend fun deleteReminder(id: String) {
        notificationManager.cancelReminderNotification(id)
        super.deleteReminder(id)
    }
} 