package com.example.ransomsensei.ui.activity_main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.ui.WelcomeActivity
import com.example.ransomsensei.ui.activity_main.components.CardSetsScreen
import com.example.ransomsensei.ui.activity_main.util.Destination
import com.example.ransomsensei.ui.cardset.AddCardSetActivity
import com.example.ransomsensei.ui.cardset.CardSetActivity
import com.example.ransomsensei.viewmodel.activity_main.CardSetsScreenViewModel
import kotlinx.serialization.Serializable
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

                NavHost(navController = navController, startDestination = MainScreenRoute) {
                    composable<Destination.CardSetsScreen> {
                        viewModel.loadCardSets()
                        CardSetsScreen(viewModel)
                    }
                }
            }
        }
    }

    private fun startAddCardSetActivity() {
        val intent = Intent(this, AddCardSetActivity::class.java)
        startActivity(intent)
    }

    private fun startCardSetActivity(cardSetId: Int) {
        val intent = Intent(this, CardSetActivity::class.java)
        intent.putExtra(CardSetActivity.CARD_SET_ID_EXTRA, cardSetId)
        startActivity(intent)
    }

    private fun startWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}

@Serializable
object MainScreenRoute