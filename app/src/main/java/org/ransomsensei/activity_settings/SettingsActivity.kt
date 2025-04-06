package org.ransomsensei.activity_settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import org.ransomsensei.activity_settings.components.SettingsScreen
import org.ransomsensei.activity_settings.util.Destination
import org.ransomsensei.activity_settings.viewmodels.SettingsViewModel
import org.ransomsensei.theme.AppTheme

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            AppTheme {
                NavHost(navController, Destination.SettingsScreen) {
                    composable<Destination.SettingsScreen> {
                        val viewModel = koinViewModel<SettingsViewModel>()
                        SettingsScreen(viewModel, navController)
                    }
                    composable<Destination.Finish> {
                        finish()
                    }
                }
            }
        }
    }
}