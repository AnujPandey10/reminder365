package com.remindme365.app.data

import android.content.ContentResolver
import android.provider.CalendarContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class CalendarReader(private val contentResolver: ContentResolver) {
    actual suspend fun getEvents(): List<CalendarEvent> = withContext(Dispatchers.IO) {
        val events = mutableListOf<CalendarEvent>()
        val cursor = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndex(CalendarContract.Events._ID))
                val title = it.getString(it.getColumnIndex(CalendarContract.Events.TITLE))
                val startDate = it.getLong(it.getColumnIndex(CalendarContract.Events.DTSTART))
                val endDate = it.getLong(it.getColumnIndex(CalendarContract.Events.DTEND))
                events.add(CalendarEvent(id, title, startDate, endDate))
            }
        }
        events
    }
}
