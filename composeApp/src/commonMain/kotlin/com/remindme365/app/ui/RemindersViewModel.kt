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

    init {
        loadReminders()
    }

    fun loadReminders() {
        viewModelScope.launch {
            _state.value = RemindersState.Loading
            try {
                val reminders = repository.getActiveReminders()
                _state.value = RemindersState.Success(reminders)
            } catch (e: Exception) {
                _state.value = RemindersState.Error(e.message ?: "Failed to load reminders")
            }
        }
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