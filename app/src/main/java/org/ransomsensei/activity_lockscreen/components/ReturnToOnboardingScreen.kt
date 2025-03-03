package org.ransomsensei.activity_lockscreen.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.ransomsensei.activity_lockscreen.util.Destination
import org.ransomsensei.theme.AppTheme

@Composable
fun ReturnToOnboardingScreen(navHostController: NavHostController) {
    AppTheme {
        ReturnToOnboardingScreen(navHostController::navigate)
    }
}

@Composable
private fun ReturnToOnboardingScreen(navigate: (Destination) -> Unit) {
    Scaffold {padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ransom Sensei is now your home screen")
            Spacer(Modifier.height(10.dp))
            Button(onClick = { navigate(Destination.Onboarding) }) { Text(text = "Return to onboarding") }
        }
    }
}

@PreviewLightDark
@Composable
fun ReturnToOnboardingScreenPreview() {
    AppTheme {
        ReturnToOnboardingScreen(navigate = { })
    }
}