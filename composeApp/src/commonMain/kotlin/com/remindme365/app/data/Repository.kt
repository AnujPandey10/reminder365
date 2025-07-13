package com.remindme365.app.data

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

open class Repository : KoinComponent {

    private val localDataSource: LocalDataSource by inject()
    private val remoteDataSource: RemoteDataSource by inject()

    // Event operations
    open suspend fun getAllEvents(): List<Event> {
        return localDataSource.getAllEvents()
    }

    open suspend fun getActiveEvents(): List<Event> {
        return localDataSource.getActiveEvents()
    }

    open suspend fun getEventById(id: String): Event? {
        return localDataSource.getEventById(id)
    }

    open suspend fun saveEvent(event: Event): Event {
        val eventWithId = if (event.id.isEmpty()) {
            event.copy(id = generateId())
        } else {
            event
        }
        localDataSource.saveEvent(eventWithId)
        
        // Schedule notification for the event
        scheduleEventNotification(eventWithId)
        
        return eventWithId
    }

    open suspend fun updateEvent(event: Event) {
        localDataSource.updateEvent(event)
        
        // Reschedule notification for the updated event
        scheduleEventNotification(event)
    }

    open suspend fun deleteEvent(id: String) {
        // Cancel notification before deleting
        cancelEventNotification(id)
        localDataSource.deleteEvent(id)
    }

    // Reminder operations
    open suspend fun getAllReminders(): List<Reminder> {
        return localDataSource.getAllReminders()
    }

    open suspend fun getActiveReminders(): List<Reminder> {
        return localDataSource.getActiveReminders()
    }

    open suspend fun getReminderById(id: String): Reminder? {
        return localDataSource.getReminderById(id)
    }

    open suspend fun saveReminder(reminder: Reminder): Reminder {
        val reminderWithId = if (reminder.id.isEmpty()) {
            reminder.copy(id = generateId())
        } else {
            reminder
        }
        localDataSource.saveReminder(reminderWithId)
        
        // Schedule notification for the reminder
        scheduleReminderNotification(reminderWithId)
        
        return reminderWithId
    }

    open suspend fun updateReminder(reminder: Reminder) {
        localDataSource.updateReminder(reminder)
        
        // Reschedule notification for the updated reminder
        scheduleReminderNotification(reminder)
    }

    open suspend fun deleteReminder(id: String) {
        // Cancel notification before deleting
        cancelReminderNotification(id)
        localDataSource.deleteReminder(id)
    }

    // UserSettings operations
    open suspend fun getUserSettings(userId: String): UserSettings {
        return localDataSource.getUserSettings(userId) ?: UserSettings()
    }

    open suspend fun saveUserSettings(settings: UserSettings, userId: String) {
        localDataSource.saveUserSettings(settings, userId)
    }

    open suspend fun updateUserSettings(settings: UserSettings, userId: String) {
        localDataSource.updateUserSettings(settings, userId)
    }

    private fun generateId(): String {
        val timestamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "id_${timestamp.toString().replace(":", "").replace("-", "").replace("T", "")}_${Random.nextInt(1000, 9999)}"
    }
    
    // Notification scheduling methods (platform-specific)
    open fun scheduleEventNotification(event: Event) {
        // This will be implemented differently for each platform
        // For now, it's a placeholder
    }
    
    open fun scheduleReminderNotification(reminder: Reminder) {
        // This will be implemented differently for each platform
        // For now, it's a placeholder
    }
    
    open fun cancelEventNotification(eventId: String) {
        // This will be implemented differently for each platform
        // For now, it's a placeholder
    }
    
    open fun cancelReminderNotification(reminderId: String) {
        // This will be implemented differently for each platform
        // For now, it's a placeholder
    }
}