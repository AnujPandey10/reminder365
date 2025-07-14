package com.remindme365.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindme365.app.data.Reminder
import com.remindme365.app.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class RemindersState {
    object Loading : RemindersState()
    data class Success(val reminders: List<Reminder>) : RemindersState()
    data class Error(val message: String) : RemindersState()
}

class RemindersViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()
    
    private val _state = MutableStateFlow<RemindersState>(RemindersState.Loading)
    val state: StateFlow<RemindersState> = _state.asStateFlow()

    private var allReminders: List<Reminder> = emptyList()

    init {
        loadReminders()
    }

    fun loadReminders() {
        viewModelScope.launch {
            _state.value = RemindersState.Loading
            try {
                allReminders = repository.getActiveReminders()
                _state.value = RemindersState.Success(allReminders)
            } catch (e: Exception) {
                _state.value = RemindersState.Error(e.message ?: "Failed to load reminders")
            }
        }
    }

    fun search(query: String) {
        val filteredReminders = allReminders.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
        _state.value = RemindersState.Success(filteredReminders)
    }

    fun refresh() {
        loadReminders()
    }

    fun deleteReminder(reminderId: String) {
        viewModelScope.launch {
            try {
                repository.deleteReminder(reminderId)
                loadReminders() // Refresh the list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                repository.updateReminder(reminder)
                loadReminders() // Refresh the list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
} 