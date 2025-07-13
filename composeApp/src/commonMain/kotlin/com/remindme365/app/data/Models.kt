package com.remindme365.app.data

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Serializable
data class Event(
    val id: String = "",
    val name: String,
    val date: LocalDate,
    val type: EventType,
    val relation: String = "",
    val notes: String = "",
    val photoUrl: String? = null,
    val email: String? = null,
    val notificationTiming: NotificationTiming = NotificationTiming.ON_DAY,
    val notificationType: NotificationType = NotificationType.APP_POPUP,
    val isActive: Boolean = true,
    val isRecurring: Boolean = false, // New field for recurring events
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)

@Serializable
data class Reminder(
    val id: String = "",
    val title: String,
    val description: String = "",
    val dateTime: LocalDateTime,
    val notificationTiming: NotificationTiming = NotificationTiming.ON_DAY,
    val notificationType: NotificationType = NotificationType.APP_POPUP,
    val isActive: Boolean = true,
    val isRecurring: Boolean = false,
    val recurringInterval: RecurringInterval? = null,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)

@Serializable
enum class EventType {
    BIRTHDAY,
    ANNIVERSARY,
    CUSTOM
}

@Serializable
enum class NotificationTiming {
    ON_DAY,
    DAY_BEFORE,
    WEEK_BEFORE
}

@Serializable
enum class NotificationType {
    APP_POPUP,
    EMAIL,
    BOTH
}

@Serializable
enum class RecurringInterval {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}

@Serializable
data class UserSettings(
    val defaultNotificationTiming: NotificationTiming = NotificationTiming.ON_DAY,
    val defaultNotificationType: NotificationType = NotificationType.APP_POPUP,
    val isPremium: Boolean = false,
    val autoSendWishes: Boolean = false,
    val multipleEmails: List<String> = emptyList()
) 