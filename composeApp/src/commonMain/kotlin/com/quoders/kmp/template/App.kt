package com.quoders.kmp.template

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.quoders.kmp.template.navigation.HomeScreenNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        HomeScreenNavigation()
    }
}