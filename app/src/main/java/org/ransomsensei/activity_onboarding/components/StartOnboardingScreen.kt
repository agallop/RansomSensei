package org.ransomsensei.activity_onboarding.components

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
import org.ransomsensei.activity_onboarding.viewmodels.StartOnboardingViewModel
import org.ransomsensei.theme.AppTheme

@Composable
fun StartOnBoardingScreen(
    viewModel: StartOnboardingViewModel,
    navHostController: NavHostController
) {
    StartOnBoardingScreen(
        navigateToNextScreen = {
            navHostController.navigate(viewModel.nextDestination)
        }
    )
}

@Composable
fun StartOnBoardingScreen(navigateToNextScreen: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Welcome to Ransom Sensei")
            Text("Let's get your app set up")
            Spacer(Modifier.height(40.dp))
            Button(onClick = navigateToNextScreen) {
                Text("Continue")
            }
        }

    }
}

@PreviewLightDark()
@Composable
fun StartOnBoardingScreenPreview() {
    AppTheme {
        StartOnBoardingScreen(
            navigateToNextScreen = {}
        )
    }
}