package com.remindme365.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindme365.app.data.Event
import com.remindme365.app.data.EventType
import com.remindme365.app.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EventsViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    private val _uiState = MutableStateFlow<EventsState>(EventsState.Loading)
    val state: StateFlow<EventsState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EventsState.Loading
        )

    private val _selectedEventType = MutableStateFlow(EventType.BIRTHDAY)
    val selectedEventType: StateFlow<EventType> = _selectedEventType

    private var allEvents: List<Event> = emptyList()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = EventsState.Loading
            try {
                allEvents = repository.getActiveEvents()
                val filteredEvents = allEvents.filter { it.type == _selectedEventType.value }
                _uiState.value = EventsState.Success(filteredEvents)
            } catch (e: Exception) {
                _uiState.value = EventsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun selectEventType(eventType: EventType) {
        _selectedEventType.value = eventType
        val filteredEvents = allEvents.filter { it.type == _selectedEventType.value }
        _uiState.value = EventsState.Success(filteredEvents)
    }

    fun search(query: String) {
        val filteredEvents = allEvents.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.notes.contains(query, ignoreCase = true) ||
                    it.relation.contains(query, ignoreCase = true)
        }
        _uiState.value = EventsState.Success(filteredEvents)
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            try {
                repository.deleteEvent(eventId)
                loadEvents()
            } catch (e: Exception) {
                _uiState.value = EventsState.Error(e.message ?: "Failed to delete event")
            }
        }
    }

    fun refresh() {
        loadEvents()
    }

    fun loadContacts() {
        viewModelScope.launch {
            try {
                // This will be resolved at the platform level
                val contactReader: com.remindme365.app.data.ContactReader by inject()
                val contacts = contactReader.getContacts()
                _uiState.value = EventsState.Import(contacts, ImportType.CONTACTS)
            } catch (e: Exception) {
                _uiState.value = EventsState.Error(e.message ?: "Failed to load contacts")
            }
        }
    }

    fun loadCalendarEvents() {
        viewModelScope.launch {
            try {
                // This will be resolved at the platform level
                val calendarReader: com.remindme365.app.data.CalendarReader by inject()
                val events = calendarReader.getEvents()
                _uiState.value = EventsState.Import(events, ImportType.CALENDAR)
            } catch (e: Exception) {
                _uiState.value = EventsState.Error(e.message ?: "Failed to load calendar events")
            }
        }
    }

    fun importEvents(events: List<Any>) {
        viewModelScope.launch {
            try {
                for (item in events) {
                    when (item) {
                        is com.remindme365.app.data.Contact -> {
                            if (item.birthday != null) {
                                val event = Event(
                                    id = item.id,
                                    name = item.name,
                                    date = kotlinx.datetime.LocalDate.parse(item.birthday),
                                    type = EventType.BIRTHDAY,
                                    relation = "Contact",
                                    notes = "",
                                    photoUrl = null,
                                    email = "",
                                    notificationTiming = com.remindme365.app.data.NotificationTiming.ON_DAY,
                                    notificationType = com.remindme365.app.data.NotificationType.APP_POPUP,
                                    isActive = true,
                                    isRecurring = true,
                                    createdAt = kotlinx.datetime.Clock.System.now()
                                )
                                repository.addEvent(event)
                            }
                        }
                        is com.remindme365.app.data.CalendarEvent -> {
                            val event = Event(
                                id = item.id,
                                name = item.title,
                                date = kotlinx.datetime.Instant.fromEpochMilliseconds(item.startDate).toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date,
                                type = EventType.CUSTOM,
                                relation = "Calendar",
                                notes = "",
                                photoUrl = null,
                                email = "",
                                notificationTiming = com.remindme365.app.data.NotificationTiming.ON_DAY,
                                notificationType = com.remindme365.app.data.NotificationType.APP_POPUP,
                                isActive = true,
                                isRecurring = false,
                                createdAt = kotlinx.datetime.Clock.System.now()
                            )
                            repository.addEvent(event)
                        }
                    }
                }
                loadEvents()
            } catch (e: Exception) {
                _uiState.value = EventsState.Error(e.message ?: "Failed to import events")
            }
        }
    }
}

sealed class EventsState {
    data object Loading : EventsState()
    data class Success(val events: List<Event>) : EventsState()
    data class Error(val message: String) : EventsState()
    data class Import<T>(val items: List<T>, val type: ImportType) : EventsState()
}

enum class ImportType {
    CONTACTS,
    CALENDAR
}