package com.quoders.kmp.template

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quoders.kmp.template.feature.screen2.RoutesViewModel
import com.quoders.kmp.template.navigation.HomeScreenNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel: RoutesViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

        HomeScreenNavigation()
    }
}