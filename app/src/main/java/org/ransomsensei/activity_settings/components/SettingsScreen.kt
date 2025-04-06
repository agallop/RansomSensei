package org.ransomsensei.activity_settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import org.ransomsensei.activity_settings.util.Destination
import org.ransomsensei.activity_settings.viewmodels.SettingsViewModel
import org.ransomsensei.theme.AppTheme

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, navController: NavController) {
    SettingsScreen(navigate = navController::navigate)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navigate: (Destination) -> Unit) {
    Scaffold (
        topBar = { CenterAlignedTopAppBar(
            title = { Text(text = "Settings") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            navigationIcon = {
                IconButton(onClick = { navigate(Destination.Finish) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                }
            })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

        }
    }
}

@Composable
@PreviewLightDark
fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(navigate = {})
    }
}