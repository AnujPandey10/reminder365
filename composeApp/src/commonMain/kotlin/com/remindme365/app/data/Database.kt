package com.remindme365.app.data

import com.remindme365.app.cache.Database
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory.createDriver())
    private val dbQuery = database.databaseQueries

    // Event operations
    internal fun getAllEvents(): List<Event> {
        return dbQuery.selectAllEvents(::mapEventSelecting).executeAsList()
    }

    internal fun getActiveEvents(): List<Event> {
        return dbQuery.selectActiveEvents(::mapEventSelecting).executeAsList()
    }

    internal fun getEventById(id: String): Event? {
        return dbQuery.selectEventById(id, ::mapEventSelecting).executeAsOneOrNull()
    }

    internal fun insertEvent(event: Event) {
        dbQuery.insertEvent(
            id = event.id,
            name = event.name,
            date = event.date.toString(),
            type = event.type.name,
            relation = event.relation,
            notes = event.notes,
            photoUrl = event.photoUrl,
            email = event.email,
            notificationTiming = event.notificationTiming.name,
            notificationType = event.notificationType.name,
            isActive = if (event.isActive) 1 else 0,
            isRecurring = if (event.isRecurring) 1 else 0,
            createdAt = event.createdAt.toString()
        )
    }

    internal fun updateEvent(event: Event) {
        dbQuery.updateEvent(
            name = event.name,
            date = event.date.toString(),
            type = event.type.name,
            relation = event.relation,
            notes = event.notes,
            photoUrl = event.photoUrl,
            email = event.email,
            notificationTiming = event.notificationTiming.name,
            notificationType = event.notificationType.name,
            isActive = if (event.isActive) 1 else 0,
            isRecurring = if (event.isRecurring) 1 else 0,
            id = event.id
        )
    }

    internal fun deleteEvent(id: String) {
        dbQuery.deleteEvent(id)
    }

    // Reminder operations
    internal fun getAllReminders(): List<Reminder> {
        return dbQuery.selectAllReminders(::mapReminderSelecting).executeAsList()
    }

    internal fun getActiveReminders(): List<Reminder> {
        return dbQuery.selectActiveReminders(::mapReminderSelecting).executeAsList()
    }

    internal fun getReminderById(id: String): Reminder? {
        return dbQuery.selectReminderById(id, ::mapReminderSelecting).executeAsOneOrNull()
    }

    internal fun insertReminder(reminder: Reminder) {
        dbQuery.insertReminder(
            id = reminder.id,
            title = reminder.title,
            description = reminder.description,
            dateTime = reminder.dateTime.toString(),
            notificationTiming = reminder.notificationTiming.name,
            notificationType = reminder.notificationType.name,
            isActive = if (reminder.isActive) 1 else 0,
            isRecurring = if (reminder.isRecurring) 1 else 0,
            recurringInterval = reminder.recurringInterval?.name,
            createdAt = reminder.createdAt.toString()
        )
    }

    internal fun updateReminder(reminder: Reminder) {
        dbQuery.updateReminder(
            title = reminder.title,
            description = reminder.description,
            dateTime = reminder.dateTime.toString(),
            notificationTiming = reminder.notificationTiming.name,
            notificationType = reminder.notificationType.name,
            isActive = if (reminder.isActive) 1 else 0,
            isRecurring = if (reminder.isRecurring) 1 else 0,
            recurringInterval = reminder.recurringInterval?.name,
            id = reminder.id
        )
    }

    internal fun deleteReminder(id: String) {
        dbQuery.deleteReminder(id)
    }

    // UserSettings operations
    internal fun getUserSettings(userId: String): UserSettings? {
        return dbQuery.selectUserSettings(userId, ::mapUserSettingsSelecting).executeAsOneOrNull()
    }

    internal fun insertUserSettings(settings: UserSettings, userId: String) {
        dbQuery.insertUserSettings(
            id = userId,
            defaultNotificationTiming = settings.defaultNotificationTiming.name,
            defaultNotificationType = settings.defaultNotificationType.name,
            isPremium = if (settings.isPremium) 1 else 0,
            autoSendWishes = if (settings.autoSendWishes) 1 else 0,
            multipleEmails = settings.multipleEmails.joinToString(",")
        )
    }

    internal fun updateUserSettings(settings: UserSettings, userId: String) {
        dbQuery.updateUserSettings(
            defaultNotificationTiming = settings.defaultNotificationTiming.name,
            defaultNotificationType = settings.defaultNotificationType.name,
            isPremium = if (settings.isPremium) 1 else 0,
            autoSendWishes = if (settings.autoSendWishes) 1 else 0,
            multipleEmails = settings.multipleEmails.joinToString(","),
            id = userId
        )
    }

    private fun mapEventSelecting(
        id: String,
        name: String,
        date: String,
        type: String,
        relation: String,
        notes: String,
        photoUrl: String?,
        email: String?,
        notificationTiming: String,
        notificationType: String,
        isActive: Long,
        isRecurring: Long,
        createdAt: String
    ): Event {
        return Event(
            id = id,
            name = name,
            date = LocalDate.parse(date),
            type = EventType.valueOf(type),
            relation = relation,
            notes = notes,
            photoUrl = photoUrl,
            email = email,
            notificationTiming = NotificationTiming.valueOf(notificationTiming),
            notificationType = NotificationType.valueOf(notificationType),
            isActive = isActive == 1L,
            isRecurring = isRecurring == 1L,
            createdAt = LocalDateTime.parse(createdAt)
        )
    }

    private fun mapReminderSelecting(
        id: String,
        title: String,
        description: String,
        dateTime: String,
        notificationTiming: String,
        notificationType: String,
        isActive: Long,
        isRecurring: Long,
        recurringInterval: String?,
        createdAt: String
    ): Reminder {
        return Reminder(
            id = id,
            title = title,
            description = description,
            dateTime = LocalDateTime.parse(dateTime),
            notificationTiming = NotificationTiming.valueOf(notificationTiming),
            notificationType = NotificationType.valueOf(notificationType),
            isActive = isActive == 1L,
            isRecurring = isRecurring == 1L,
            recurringInterval = recurringInterval?.let { RecurringInterval.valueOf(it) },
            createdAt = LocalDateTime.parse(createdAt)
        )
    }

    private fun mapUserSettingsSelecting(
        id: String,
        defaultNotificationTiming: String,
        defaultNotificationType: String,
        isPremium: Long,
        autoSendWishes: Long,
        multipleEmails: String
    ): UserSettings {
        return UserSettings(
            defaultNotificationTiming = NotificationTiming.valueOf(defaultNotificationTiming),
            defaultNotificationType = NotificationType.valueOf(defaultNotificationType),
            isPremium = isPremium == 1L,
            autoSendWishes = autoSendWishes == 1L,
            multipleEmails = if (multipleEmails.isNotEmpty()) multipleEmails.split(",") else emptyList()
        )
    }
}