package com.example.ransomsensei.ui.activity_main

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
import com.example.ransomsensei.ui.WelcomeActivity
import com.example.ransomsensei.ui.activity_main.components.AddEditCardSetScreen
import com.example.ransomsensei.ui.activity_main.components.CardSetDetailsScreen
import com.example.ransomsensei.ui.activity_main.components.CardSetsScreen
import com.example.ransomsensei.ui.activity_main.util.Destination
import com.example.ransomsensei.viewmodel.activity_main.AddEditCardSetScreenViewModel
import com.example.ransomsensei.viewmodel.activity_main.CardSetDetailsScreenViewModel
import com.example.ransomsensei.viewmodel.activity_main.CardSetsScreenViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val viewModel = koinViewModel<CardSetsScreenViewModel>()

                LaunchedEffect(key1 = viewModel.needToSetHomeActivity) {
                    if (viewModel.needToSetHomeActivity) {
                        startWelcomeActivity()
                    }
                }

                NavHost(navController = navController, startDestination = Destination.CardSetsScreen) {
                    composable<Destination.CardSetsScreen> {
                        viewModel.loadCardSets()
                        CardSetsScreen(navController, viewModel)
                    }
                    composable<Destination.AddEditCardSetScreen> {
                        val args = it.toRoute<Destination.AddEditCardSetScreen>()
                        val addEditCardSetScreenViewModel = koinViewModel<AddEditCardSetScreenViewModel>()
                        when {args.cardSetId != null ->
                            addEditCardSetScreenViewModel.loadCardSet(args.cardSetId)
                        }
                        AddEditCardSetScreen(navController, addEditCardSetScreenViewModel)
                    }
                    composable<Destination.CardSetDetailsScreen> {
                        val args = it.toRoute<Destination.CardSetDetailsScreen>()
                        val cardSetDetailsScreenViewModel = koinViewModel<CardSetDetailsScreenViewModel>()
                        cardSetDetailsScreenViewModel.loadCards(args.cardSetId)
                        CardSetDetailsScreen(navController, cardSetDetailsScreenViewModel)
                    }
                }
            }
        }
    }

    private fun startWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}