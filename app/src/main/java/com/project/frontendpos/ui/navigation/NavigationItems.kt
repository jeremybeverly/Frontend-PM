package com.project.frontendpos.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable object LoginRoute
@Serializable object HomeRoute
@Serializable object ShiftRoute
@Serializable object HistoryRoute

@Serializable object CheckoutRoute
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Any
)

val bottomNavItems = listOf(
    BottomNavItem("Beranda", Icons.Default.Home, HomeRoute),
    BottomNavItem("Shift Kerja", Icons.Default.Schedule, ShiftRoute),
    BottomNavItem("Riwayat", Icons.Default.History, HistoryRoute)
)