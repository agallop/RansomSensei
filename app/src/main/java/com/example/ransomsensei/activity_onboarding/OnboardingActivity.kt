package com.example.ransomsensei.activity_onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ransomsensei.activity_onboarding.components.SetDefaultHomeAppScreen
import com.example.ransomsensei.activity_onboarding.components.SetHomeActivityScreen
import com.example.ransomsensei.activity_onboarding.components.StartOnBoardingScreen
import com.example.ransomsensei.activity_onboarding.util.Destination
import com.example.ransomsensei.activity_onboarding.viewmodels.OnboardingActivityViewModel
import com.example.ransomsensei.activity_onboarding.viewmodels.SetDefaultHomeAppViewModel
import com.example.ransomsensei.activity_onboarding.viewmodels.SetHomeActivityViewModel
import com.example.ransomsensei.activity_onboarding.viewmodels.StartOnboardingViewModel
import com.example.ransomsensei.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val activityViewModel = koinViewModel<OnboardingActivityViewModel>()
            val navHostController = rememberNavController()

            AppTheme {
                if (activityViewModel.isLoading) {
                    Scaffold { padding ->
                        Box(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.width(64.dp))
                        }
                    }
                } else {
                    NavHost(
                        navController = navHostController,
                        startDestination = Destination.StartOnBoardingScreen
                    ) {
                        println(activityViewModel.destinationGraph)
                        composable<Destination.StartOnBoardingScreen> {
                            val screenViewModel = koinViewModel<StartOnboardingViewModel>()
                            screenViewModel.nextDestination =
                                activityViewModel
                                    .destinationGraph[Destination.StartOnBoardingScreen]
                                    ?: Destination.Finish
                            StartOnBoardingScreen(
                                navHostController = navHostController, viewModel = screenViewModel
                            )
                        }
                        composable<Destination.SetHomeActivityScreen> {
                            val screenViewModel = koinViewModel<SetHomeActivityViewModel>()
                            screenViewModel.nextDestination =
                                activityViewModel
                                    .destinationGraph[Destination.SetHomeActivityScreen]
                                    ?: Destination.Finish
                            SetHomeActivityScreen(
                                navHostController = navHostController, viewModel = screenViewModel
                            )
                        }
                        composable<Destination.SetDefaultHomeAppScreen> {
                            val screenViewModel = koinViewModel<SetDefaultHomeAppViewModel>()
                            screenViewModel.nextDestination =
                                activityViewModel
                                    .destinationGraph[Destination.SetDefaultHomeAppScreen]
                                    ?: Destination.Finish
                            SetDefaultHomeAppScreen(
                                context = this@OnboardingActivity,
                                navHostController = navHostController,
                                viewModel = screenViewModel
                            )
                        }
                        composable<Destination.Finish> {
                            finish()
                        }
                    }
                }
            }
        }
    }
}