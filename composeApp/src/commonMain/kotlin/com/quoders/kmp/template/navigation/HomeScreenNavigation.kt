package com.quoders.kmp.template.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object AroundMe : Screen("aroundme", "Around me", Icons.Default.Home)
    data object Routes : Screen("routes", "Routes", Icons.AutoMirrored.Default.List)
    data object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
}
