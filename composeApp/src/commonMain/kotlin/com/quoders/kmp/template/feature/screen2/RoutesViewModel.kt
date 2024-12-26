package com.quoders.kmp.template.feature.screen2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quoders.kmp.template.Route
import com.quoders.kmp.template.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RoutesViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    private val _uiState = MutableStateFlow<RoutesModelState>(RoutesModelState.Loading)
    val state: StateFlow<RoutesModelState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RoutesModelState.Loading
        )

    init {
        onGreetingChange()
    }

    private fun onGreetingChange() {
        viewModelScope.launch {
            _uiState.update { RoutesModelState.Loading }
            try {
                val routes = repository.getRoutes()
                _uiState.update { RoutesModelState.Result(routes) }
            } catch (e: Exception) {
                _uiState.update { RoutesModelState.Error(e) }
            }
        }
    }
}

sealed class RoutesModelState {
    data object Loading : RoutesModelState()
    data class Result(val routes: List<Route>) : RoutesModelState()
    data class Error(val error: Throwable) : RoutesModelState()
}