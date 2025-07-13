package com.remindme365.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindme365.app.data.Reminder
import com.remindme365.app.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemindersViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    private val _uiState = MutableStateFlow<RemindersState>(RemindersState.Loading)
    val state: StateFlow<RemindersState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RemindersState.Loading
        )

    init {
        loadReminders()
    }

    private fun loadReminders() {
        viewModelScope.launch {
            _uiState.value = RemindersState.Loading
            try {
                val reminders = repository.getActiveReminders()
                _uiState.value = RemindersState.Success(reminders)
            } catch (e: Exception) {
                _uiState.value = RemindersState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteReminder(reminderId: String) {
        viewModelScope.launch {
            try {
                repository.deleteReminder(reminderId)
                loadReminders()
            } catch (e: Exception) {
                _uiState.value = RemindersState.Error(e.message ?: "Failed to delete reminder")
            }
        }
    }

    fun refresh() {
        loadReminders()
    }
}

sealed class RemindersState {
    data object Loading : RemindersState()
    data class Success(val reminders: List<Reminder>) : RemindersState()
    data class Error(val message: String) : RemindersState()
} 