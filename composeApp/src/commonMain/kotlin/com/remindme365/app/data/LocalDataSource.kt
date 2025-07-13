package com.remindme365.app.data

class LocalDataSource(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)

    // Event operations
    fun getAllEvents() = database.getAllEvents()
    fun getActiveEvents() = database.getActiveEvents()
    fun getEventById(id: String) = database.getEventById(id)
    fun saveEvent(event: Event) = database.insertEvent(event)
    fun updateEvent(event: Event) = database.updateEvent(event)
    fun deleteEvent(id: String) = database.deleteEvent(id)

    // Reminder operations
    fun getAllReminders() = database.getAllReminders()
    fun getActiveReminders() = database.getActiveReminders()
    fun getReminderById(id: String) = database.getReminderById(id)
    fun saveReminder(reminder: Reminder) = database.insertReminder(reminder)
    fun updateReminder(reminder: Reminder) = database.updateReminder(reminder)
    fun deleteReminder(id: String) = database.deleteReminder(id)

    // UserSettings operations
    fun getUserSettings(userId: String) = database.getUserSettings(userId)
    fun saveUserSettings(settings: UserSettings, userId: String) = database.insertUserSettings(settings, userId)
    fun updateUserSettings(settings: UserSettings, userId: String) = database.updateUserSettings(settings, userId)
}