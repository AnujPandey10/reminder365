package com.quoders.kmp.bizkaimoves.feature.routes

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun RoutesScreen() {
    val viewModel: RoutesViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    RoutesContent(state)
}

@Composable
fun RoutesContent(state: RoutesModelState) {
    when (state) {
        is RoutesModelState.Loading -> Text("Loading...")
        is RoutesModelState.Result -> Text("Result: ${state.routes}")
        is RoutesModelState.Error -> Text("Error: ${state.error}")
    }
}
