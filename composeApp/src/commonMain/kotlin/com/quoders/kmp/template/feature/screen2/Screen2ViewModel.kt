package com.quoders.kmp.template.feature.screen2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quoders.kmp.template.Album
import com.quoders.kmp.template.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Screen2ViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    private val _uiState = MutableStateFlow<Screen2State>(Screen2State.Loading)
    val state: StateFlow<Screen2State> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Screen2State.Loading
        )

    init {
        fetchAlbums()
    }

    private fun fetchAlbums() {
        viewModelScope.launch {
            _uiState.update { Screen2State.Loading }
            try {
                val routes = repository.getAlbums()
                _uiState.update { Screen2State.Result(routes) }
            } catch (e: Exception) {
                _uiState.update { Screen2State.Error(e) }
            }
        }
    }
}

sealed class Screen2State {
    data object Loading : Screen2State()
    data class Result(val albums: List<Album>) : Screen2State()
    data class Error(val error: Throwable) : Screen2State()
}