package com.remindme365.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindme365.app.data.Event
import com.remindme365.app.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDate

class HomeViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    private val _uiState = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeState.Loading
        )

    init {
        loadUpcomingEvents()
    }

    private fun loadUpcomingEvents() {
        viewModelScope.launch {
            _uiState.value = HomeState.Loading
            try {
                val events = repository.getActiveEvents()
                val upcomingEvents = getUpcomingEvents(events)
                _uiState.value = HomeState.Success(upcomingEvents)
            } catch (e: Exception) {
                _uiState.value = HomeState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun getUpcomingEvents(events: List<Event>): List<Event> {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return events
            .filter { it.date >= currentDate }
            .sortedBy { it.date }
            .take(10) // Show next 10 upcoming events
    }

    fun refresh() {
        loadUpcomingEvents()
    }
    
    // Call this when a new event is added
    fun onEventAdded() {
        loadUpcomingEvents()
    }
    
    // Delete an event and refresh the list
    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            try {
                repository.deleteEvent(eventId)
                loadUpcomingEvents() // Refresh the list after deletion
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }
}

sealed class HomeState {
    data object Loading : HomeState()
    data class Success(val events: List<Event>) : HomeState()
    data class Error(val message: String) : HomeState()
} 