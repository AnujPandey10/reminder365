package com.quoders.kmp.template.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Screen1 : Screen("screen1", "Screen1", Icons.Default.Home)
    data object Screen2 : Screen("screen2", "Screen2", Icons.AutoMirrored.Default.List)
    data object Screen3 : Screen("screen3", "Screen3", Icons.Default.Favorite)
}
