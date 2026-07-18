package com.project.frontendpos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.frontendpos.ui.features.login.LoginScreen
import com.project.frontendpos.ui.features.products.ProductScreen
import kotlinx.serialization.Serializable

// Using type-safe Navigation routes
@Serializable object LoginDest
@Serializable object ProductDest

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginDest) {
        composable<LoginDest> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(ProductDest) {
                        popUpTo<LoginDest> { inclusive = true } // Wipe out auth history stack
                    }
                }
            )
        }
        composable<ProductDest> {
            ProductScreen()
        }
    }
}