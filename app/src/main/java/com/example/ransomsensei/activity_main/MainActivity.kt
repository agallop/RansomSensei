package com.example.ransomsensei.activity_main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.activity_onboarding.OnboardingActivity
import com.example.ransomsensei.activity_main.components.AddEditCardScreen
import com.example.ransomsensei.activity_main.components.AddEditCardSetScreen
import com.example.ransomsensei.activity_main.components.CardSetDetailsScreen
import com.example.ransomsensei.activity_main.components.CardSetsScreen
import com.example.ransomsensei.activity_main.util.Destination
import com.example.ransomsensei.activity_main.viewmodels.AddEditCardViewModel
import com.example.ransomsensei.activity_main.viewmodels.AddEditCardSetViewModel
import com.example.ransomsensei.activity_main.viewmodels.CardSetDetailsViewModel
import com.example.ransomsensei.activity_main.viewmodels.CardSetsViewModel
import com.example.ransomsensei.activity_main.viewmodels.MainActivityViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val mainActivityViewModel = koinViewModel<MainActivityViewModel>()

                LaunchedEffect(key1 = mainActivityViewModel.needToLaunchOnboardingActivity) {
                    if (mainActivityViewModel.needToLaunchOnboardingActivity) {
                        startOnboardingActivity()
                    }
                }

                NavHost(navController = navController, startDestination = Destination.CardSetsScreen) {
                    composable<Destination.CardSetsScreen> {
                        val viewModel = koinViewModel<CardSetsViewModel>()
                        viewModel.loadCardSets()
                        CardSetsScreen(navController, viewModel)
                    }
                    composable<Destination.AddEditCardSetScreen> {
                        val args = it.toRoute<Destination.AddEditCardSetScreen>()
                        val addEditCardSetViewModel = koinViewModel<AddEditCardSetViewModel>()
                        when {args.cardSetId != null ->
                            addEditCardSetViewModel.loadCardSet(args.cardSetId)
                        }
                        AddEditCardSetScreen(navController, addEditCardSetViewModel)
                    }
                    composable<Destination.CardSetDetailsScreen> {
                        val args = it.toRoute<Destination.CardSetDetailsScreen>()
                        val cardSetDetailsViewModel = koinViewModel<CardSetDetailsViewModel>()
                        cardSetDetailsViewModel.loadCards(args.cardSetId)
                        CardSetDetailsScreen(navController, cardSetDetailsViewModel)
                    }
                    composable<Destination.AddEditCardScreen> {
                        val args = it.toRoute<Destination.AddEditCardScreen>()
                        val addEditCardViewModel = koinViewModel<AddEditCardViewModel>()
                        addEditCardViewModel.updateCardSetId(args.cardSetId)
                        when {args.cardId != null ->
                            addEditCardViewModel.loadCard(args.cardId)
                        }
                        AddEditCardScreen(navController, addEditCardViewModel)
                    }

                }
            }
        }
    }

    private fun startOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
    }
}