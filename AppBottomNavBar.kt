package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.TextSecondary

sealed class NavRoute(val route: String, val title: String) {
    object Home : NavRoute("home", "Home")
    object Generators : NavRoute("generators", "AI Tools")
    object ContentTools : NavRoute("content_tools", "Creator")
    object History : NavRoute("history", "History")
    object Profile : NavRoute("profile", "Profile")
}

@Composable
fun AppBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = DarkSurface,
        contentColor = Color.White,
        modifier = Modifier.testTag("app_bottom_nav_bar")
    ) {
        NavigationBarItem(
            selected = currentRoute == NavRoute.Home.route,
            onClick = { onNavigate(NavRoute.Home.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentCyan,
                selectedTextColor = AccentCyan,
                indicatorColor = PrimaryPurple.copy(alpha = 0.3f),
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )

        NavigationBarItem(
            selected = currentRoute == NavRoute.Generators.route,
            onClick = { onNavigate(NavRoute.Generators.route) },
            icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Generators") },
            label = { Text("AI Tools") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentCyan,
                selectedTextColor = AccentCyan,
                indicatorColor = PrimaryPurple.copy(alpha = 0.3f),
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )

        NavigationBarItem(
            selected = currentRoute == NavRoute.ContentTools.route,
            onClick = { onNavigate(NavRoute.ContentTools.route) },
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Creator Tools") },
            label = { Text("Creator") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentCyan,
                selectedTextColor = AccentCyan,
                indicatorColor = PrimaryPurple.copy(alpha = 0.3f),
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )

        NavigationBarItem(
            selected = currentRoute == NavRoute.History.route,
            onClick = { onNavigate(NavRoute.History.route) },
            icon = { Icon(Icons.Default.History, contentDescription = "History") },
            label = { Text("History") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentCyan,
                selectedTextColor = AccentCyan,
                indicatorColor = PrimaryPurple.copy(alpha = 0.3f),
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )

        NavigationBarItem(
            selected = currentRoute == NavRoute.Profile.route,
            onClick = { onNavigate(NavRoute.Profile.route) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentCyan,
                selectedTextColor = AccentCyan,
                indicatorColor = PrimaryPurple.copy(alpha = 0.3f),
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )
    }
}
