package com.remindme365.app.data

expect class CalendarReader {
    suspend fun getEvents(): List<CalendarEvent>
}

data class CalendarEvent(
    val id: String,
    val title: String,
    val startDate: Long,
    val endDate: Long
)
