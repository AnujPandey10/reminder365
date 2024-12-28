package com.quoders.kmp.template.feature.screen2

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun Screen2() {
    val viewModel: Screen2ViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    Screen2Content(state)
}

@Composable
fun Screen2Content(state: Screen2State) {
    when (state) {
        is Screen2State.Loading -> Text("Loading...")
        is Screen2State.Result -> Text("Result: ${state.albums}")
        is Screen2State.Error -> Text("Error: ${state.error}")
    }
}
