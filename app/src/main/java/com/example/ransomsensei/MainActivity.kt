package com.example.ransomsensei

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.ui.cardset.AddCardSetActivity
import com.example.ransomsensei.ui.cardset.CardSetActivity
import com.example.ransomsensei.viewmodel.MainScreenViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val welcomeActivityIntent = Intent(this, WelcomeActivity::class.java)

                val context = LocalContext.current
                val viewModel: MainScreenViewModel by viewModels {
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            val database = RansomSenseiDatabase.getInstance(context)
                            val dataStoreManager = RansomSenseiDataStoreManager(context)
                            return MainScreenViewModel(database, dataStoreManager) as T
                        }
                    }
                }

                LaunchedEffect(key1 = Unit) {
                    viewModel.loadCardSets()
                }

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Ransom Sensei")
                            },
                            actions =
                            {
                                if (viewModel.selectedCardSets.isEmpty()) {
                                    NoSelectedItemsNavigationBarActions(viewModel)
                                }
                                else SelectedItemsNavigationBarActions(
                                    viewModel
                                )
                            }

                        )
                    }) { padding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(viewModel.cardSets) {
                            CardSetCard(
                                cardSet = it,
                                viewModel
                            )
                        }
                    }

                    if (viewModel.needToSetHomeActivity) {
                        viewModel.showHomeActivityWelcome()
                        startActivity(welcomeActivityIntent)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CardSetCard(
        cardSet: CardSet,
        viewModel: MainScreenViewModel,
    ) {
        val haptics = LocalHapticFeedback.current
        val activity = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.reloadCardSets()
            }
        }

        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(4.dp)
                .combinedClickable(
                    onClick = {
                        val cardSetIntent = Intent(this, CardSetActivity::class.java)
                        cardSetIntent.putExtra(CardSetActivity.CARD_SET_ID_EXTRA, cardSet.cardSetId)
                        activity.launch(cardSetIntent)
                    },
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.toggleCardSetSelection(cardSet)
                    })
        ) {
            ListItem(
                colors = if (!viewModel.isCardSetSelected(cardSet))
                    ListItemDefaults.colors()
                else
                    ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                overlineContent = {
                    Text(
                        text =
                        if (cardSet.cardSetStatus == CardSetStatus.ENABLED)
                            "Active"
                        else
                            "Inactive",
                        color =
                        if (cardSet.cardSetStatus == CardSetStatus.ENABLED)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,

                    )
                },

                headlineContent = {
                    Text(
                        text = cardSet.cardSetName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                supportingContent = { Text("Terms: ${cardSet.cardCount}") },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Navigate to Set"
                    )
                })
        }
    }

    @Composable
    fun NoSelectedItemsNavigationBarActions(viewModel: MainScreenViewModel) {
        val addCardSetIntent = Intent(this, AddCardSetActivity::class.java)
        val activity = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.reloadCardSets()
            }
        }

        IconButton(onClick = {
            activity.launch(addCardSetIntent)
        }) { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add button") }
    }

    @Composable
    fun SelectedItemsNavigationBarActions(
        viewModel: MainScreenViewModel
    ) {
        IconButton(onClick = {
            viewModel.showDeleteConfirmation()
        }) { Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete button") }
        when {
            viewModel.showDeleteConfirmation->
                DeleteCardsAlertDialog(
                    cardCount = viewModel.selectedCardSets.size,
                    onConfirmation = {
                        viewModel.deleteSelectedCardSets()
                    },
                    onDismiss = { viewModel.hideDeleteConfirmation() }
                )
        }
    }

    @Composable
    fun DeleteCardsAlertDialog(
        cardCount: Int,
        onConfirmation: () -> Unit,
        onDismiss: () -> Unit = {},
    ) {
        AlertDialog(
            icon = {
                Icon(imageVector = Icons.Filled.Info, contentDescription = "Info Icon")
            },
            title = {
                Text(text = "Confirm Deletion")
            },
            text = {
                Text(
                    text = if (cardCount == 1)
                        "Are you sure you want to delete this item?"
                    else
                        "Are you sure you want to delete these $cardCount items?"
                )
            },
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmation
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}