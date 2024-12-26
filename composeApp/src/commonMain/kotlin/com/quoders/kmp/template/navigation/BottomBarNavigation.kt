package com.quoders.kmp.template.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quoders.kmp.template.feature.screen1.Screen1
import com.quoders.kmp.template.feature.screen3.FavoritesScreen
import com.quoders.kmp.template.feature.screen2.RoutesScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreenNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        NavigationHost(navController = navController)
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.AroundMe,
        Screen.Routes,
        Screen.Favorites
    )

    BottomNavigation {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.label,
                        tint = if (currentRoute == screen.route) Color.Blue else Color.Gray.copy(
                            alpha = 0.6f
                        )
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        color = if (currentRoute == screen.route) Color.Blue else Color.Gray.copy(
                            alpha = 0.6f
                        )
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.AroundMe.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.AroundMe.route) {
        composable(Screen.AroundMe.route) {
            Screen1()
        }
        composable(Screen.Routes.route) {
            RoutesScreen()
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen()
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreenNavigation() {
    MaterialTheme {
        HomeScreenNavigation()
    }
}