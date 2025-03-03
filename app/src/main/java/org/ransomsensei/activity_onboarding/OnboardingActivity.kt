package org.ransomsensei.activity_onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import org.koin.androidx.compose.koinViewModel
import org.ransomsensei.activity_main.MainActivity
import org.ransomsensei.activity_onboarding.components.OnboardingCompleteScreen
import org.ransomsensei.activity_onboarding.components.SetDefaultHomeAppScreen
import org.ransomsensei.activity_onboarding.components.SetHomeActivityScreen
import org.ransomsensei.activity_onboarding.components.StartOnBoardingScreen
import org.ransomsensei.activity_onboarding.util.Destination
import org.ransomsensei.activity_onboarding.viewmodels.OnboardingActivityViewModel
import org.ransomsensei.activity_onboarding.viewmodels.SetDefaultHomeAppViewModel
import org.ransomsensei.activity_onboarding.viewmodels.SetHomeActivityViewModel
import org.ransomsensei.activity_onboarding.viewmodels.StartOnboardingViewModel
import org.ransomsensei.theme.AppTheme

class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                        startDestination = activityViewModel.startDestination
                    ) {
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
                        composable<Destination.OnboardingCompleteScreen> {
                            OnboardingCompleteScreen(navHostController)
                        }
                        composable<Destination.Finish> {
                            activityViewModel.finishOnboarding()
                            val intent = Intent(this@OnboardingActivity, MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }
}