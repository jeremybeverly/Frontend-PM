package com.project.frontendpos.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.frontendpos.data.local.SessionManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.frontendpos.ui.screens.CheckoutScreen
import com.project.frontendpos.viewmodel.ProductViewModel
import com.project.frontendpos.viewmodel.ModifierViewModel
import com.project.frontendpos.viewmodel.CartViewModel
import com.project.frontendpos.ui.screens.LoginScreen
import com.project.frontendpos.ui.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val productViewModel: ProductViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val modifierViewModel: ModifierViewModel = viewModel()
    val showBottomBar = bottomNavItems.any { item ->
        currentDestination?.hasRoute(item.route::class) == true
    }

    val startDestination = if (SessionManager.isLoggedIn()) HomeRoute else LoginRoute

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentDestination?.hasRoute(item.route::class) == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<LoginRoute> {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(HomeRoute) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    }
                )
            }
            composable<HomeRoute> {
                HomeScreen(
                    navController = navController,
                    productViewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    modifierViewModel = modifierViewModel
                )
            }
            composable<ShiftRoute> {
                PlaceholderScreen("Shift Kerja")
            }
            composable<HistoryRoute> {
                PlaceholderScreen("Riwayat")
            }
            composable<CheckoutRoute> {
                CheckoutScreen(
                    navController = navController,
                    cartViewModel = cartViewModel
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    androidx.compose.foundation.layout.Box(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(text = "$name Screen")
    }
}
