package org.ransomsensei.activity_lockscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import org.ransomsensei.activity_lockscreen.components.LockScreen
import org.ransomsensei.activity_lockscreen.components.ReturnToOnboardingScreen
import org.ransomsensei.activity_lockscreen.util.Destination
import org.ransomsensei.activity_lockscreen.viewmodels.HomeActivityViewModel
import org.ransomsensei.activity_lockscreen.viewmodels.LockScreenViewModel
import org.ransomsensei.activity_onboarding.OnboardingActivity
import kotlin.text.isNotEmpty

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val homeActivityViewModel = koinViewModel<HomeActivityViewModel>();

            WindowCompat.setDecorFitsSystemWindows(window, false)
            val navHostController = rememberNavController()
            if (homeActivityViewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else {
                NavHost(
                    navHostController,
                    if (homeActivityViewModel.isInOnboarding)
                        Destination.ReturnToOnboardingScreen
                    else
                        Destination.LockScreen
                ) {
                    composable<Destination.LockScreen> {
                        val viewModel = koinViewModel<LockScreenViewModel>()
                        LockScreen(viewModel, navHostController)
                    }
                    composable<Destination.ReturnToOnboardingScreen> {
                        ReturnToOnboardingScreen(navHostController)
                    }
                    composable<Destination.Onboarding> {
                        returnToOnboarding()
                    }
                    composable<Destination.Finish> {
                        val args = it.toRoute<Destination.Finish>()
                        continueToHomeApp(args.homePackage)
                    }
                }
            }
        }
    }

    fun returnToOnboarding() {
        val intent = Intent(this, OnboardingActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }


    fun continueToHomeApp(homeActivityPackage: String) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        if (homeActivityPackage.isNotEmpty()) {
            intent.setPackage(homeActivityPackage)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            val chooser = Intent.createChooser(intent, /* title */ null)
            startActivity(chooser)
        }
        finish()
    }
}