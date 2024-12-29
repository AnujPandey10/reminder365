package com.quoders.kmp.template.feature.screen1

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

class Screen1ViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    private val _uiState = MutableStateFlow<Screen1State>(Screen1State.Loading)
    val state: StateFlow<Screen1State> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Screen1State.Loading
        )

    init {
        fetchAlbums()
    }

    private fun fetchAlbums() {
        viewModelScope.launch {
            _uiState.update { Screen1State.Loading }
            try {
                val albums = repository.getAlbums()
                _uiState.update { Screen1State.Result(albums) }
            } catch (e: Exception) {
                _uiState.update { Screen1State.Error(e) }
            }
        }
    }
}

sealed class Screen1State {
    data object Loading : Screen1State()
    data class Result(val albums: List<Album>) : Screen1State()
    data class Error(val error: Throwable) : Screen1State()
}