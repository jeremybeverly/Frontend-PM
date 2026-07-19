package com.project.frontendpos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.ui.navigation.AppNavigation
import com.project.frontendpos.ui.theme.FrontendPOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(applicationContext)
        setContent {
            FrontendPOSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pulls up your reactive dashboard/products view layout directly
                    AppNavigation()
                }
            }
        }
    }
}