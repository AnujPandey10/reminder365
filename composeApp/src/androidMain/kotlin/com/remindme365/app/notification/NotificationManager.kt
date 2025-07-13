package com.remindme365.app.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.remindme365.app.MainActivity
import com.remindme365.app.R
import com.remindme365.app.data.Event
import com.remindme365.app.data.Reminder
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.minus

class NotificationManager(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val CHANNEL_ID = "remindme365_channel"
        const val CHANNEL_NAME = "RemindMe365 Notifications"
        const val CHANNEL_DESCRIPTION = "Notifications for birthdays, anniversaries, and reminders"
        const val EVENT_NOTIFICATION_ID = 1000
        const val REMINDER_NOTIFICATION_ID = 2000
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleEventNotification(event: Event) {
        val notificationTime = getNotificationTime(event.date, event.notificationTiming)
        if (notificationTime > Clock.System.now()) {
            scheduleNotification(
                id = EVENT_NOTIFICATION_ID + event.id.hashCode(),
                title = "${event.name}'s ${event.type.name.lowercase().capitalizeFirst()}",
                message = "Today is ${event.name}'s ${event.type.name.lowercase()}!",
                triggerTime = notificationTime,
                eventId = event.id
            )
        }
        
        // If it's a recurring event, schedule notifications for future years
        if (event.isRecurring) {
            scheduleRecurringEventNotifications(event)
        }
    }
    
    private fun scheduleRecurringEventNotifications(event: Event) {
        val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.year
        val maxFutureYears = 10 // Schedule notifications for next 10 years
        
        for (yearOffset in 1..maxFutureYears) {
            val futureYear = currentYear + yearOffset
            val futureDate = LocalDate(futureYear, event.date.month, event.date.dayOfMonth)
            val notificationTime = getNotificationTime(futureDate, event.notificationTiming)
            
            if (notificationTime > Clock.System.now()) {
                scheduleNotification(
                    id = EVENT_NOTIFICATION_ID + event.id.hashCode() + yearOffset,
                    title = "${event.name}'s ${event.type.name.lowercase().capitalizeFirst()}",
                    message = "Today is ${event.name}'s ${event.type.name.lowercase()}!",
                    triggerTime = notificationTime,
                    eventId = event.id
                )
            }
        }
    }

    fun scheduleReminderNotification(reminder: Reminder) {
        val notificationTime = getNotificationTime(reminder.dateTime.date, reminder.notificationTiming)
        if (notificationTime > Clock.System.now()) {
            scheduleNotification(
                id = REMINDER_NOTIFICATION_ID + reminder.id.hashCode(),
                title = reminder.title,
                message = reminder.description,
                triggerTime = notificationTime,
                reminderId = reminder.id
            )
        }
    }

    private fun scheduleNotification(
        id: Int,
        title: String,
        message: String,
        triggerTime: Instant,
        eventId: String? = null,
        reminderId: String? = null
    ) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("notification_id", id)
            putExtra("title", title)
            putExtra("message", message)
            eventId?.let { putExtra("event_id", it) }
            reminderId?.let { putExtra("reminder_id", it) }
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTimeMillis = triggerTime.toEpochMilliseconds()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )
    }

    private fun getNotificationTime(date: LocalDate, timing: com.remindme365.app.data.NotificationTiming): Instant {
        val zone = TimeZone.currentSystemDefault()
        val localTime = LocalTime(hour = 9, minute = 0)
        val targetDate = when (timing) {
            com.remindme365.app.data.NotificationTiming.ON_DAY -> date
            com.remindme365.app.data.NotificationTiming.DAY_BEFORE -> date.minus(DatePeriod(days = 1))
            com.remindme365.app.data.NotificationTiming.WEEK_BEFORE -> date.minus(DatePeriod(days = 7))
        }
        return targetDate.atTime(localTime).toInstant(zone)
    }

    fun cancelEventNotification(eventId: String) {
        // Cancel the main notification
        val id = EVENT_NOTIFICATION_ID + eventId.hashCode()
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
        
        // Cancel all recurring notifications for this event
        for (yearOffset in 1..10) {
            val recurringId = EVENT_NOTIFICATION_ID + eventId.hashCode() + yearOffset
            val recurringIntent = Intent(context, NotificationReceiver::class.java)
            val recurringPendingIntent = PendingIntent.getBroadcast(
                context,
                recurringId,
                recurringIntent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            recurringPendingIntent?.let {
                alarmManager.cancel(it)
                it.cancel()
            }
        }
    }

    fun cancelReminderNotification(reminderId: String) {
        val id = REMINDER_NOTIFICATION_ID + reminderId.hashCode()
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }

    fun showNotification(title: String, message: String, id: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(id, notification)
    }
}

private fun String.capitalizeFirst(): String {
    return if (isNotEmpty()) this[0].uppercase() + substring(1) else this
} 