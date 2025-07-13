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

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = EventsState.Loading
            try {
                val events = repository.getActiveEvents()
                val filteredEvents = events.filter { it.type == _selectedEventType.value }
                _uiState.value = EventsState.Success(filteredEvents)
            } catch (e: Exception) {
                _uiState.value = EventsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun selectEventType(eventType: EventType) {
        _selectedEventType.value = eventType
        loadEvents()
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
}

sealed class EventsState {
    data object Loading : EventsState()
    data class Success(val events: List<Event>) : EventsState()
    data class Error(val message: String) : EventsState()
} 