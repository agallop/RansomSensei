package com.example.ransomsensei.ui.cardset

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.lazy.items
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.ui.card.AddCardActivity
import com.example.ransomsensei.ui.card.EditCardActivity
import com.example.ransomsensei.viewmodel.cardset.CardSetViewModel
import org.koin.androidx.compose.koinViewModel

class CardSetActivity : ComponentActivity() {

    companion object {
        const val CARD_SET_ID_EXTRA = "CARD_SET_ID"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cardSetId = intent.getIntExtra(CARD_SET_ID_EXTRA, 0)

    setContent {
        val viewModel = koinViewModel<CardSetViewModel>()

        AppTheme {
            LaunchedEffect(key1 = Unit) {
                viewModel.loadCards(cardSetId)
            }

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text(viewModel.cardSet.cardSetName)
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                if(viewModel.hasChanges)
                                    setResult(RESULT_OK)
                                finish()}) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
                            }
                        },
                        actions =
                        {
                            if (viewModel.selectedCards.isEmpty())
                                NoSelectedItemsNavigationBarActions(viewModel)
                            else
                                SelectedItemsNavigationBarActions(viewModel)
                        }
                    )
                }) { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    verticalArrangement = Arrangement.Top
                ) {
                    items(viewModel.cards) {
                        CardItem(
                            card = it,
                            viewModel
                        )
                    }
                }
            }
        }
    }}

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CardItem(
        card: Card,
        viewModel: CardSetViewModel
    ) {
        val activity = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.reloadCards()
            }
        }
        val haptics = LocalHapticFeedback.current
        val editTermActivityIntent = Intent(this, EditCardActivity::class.java)
        editTermActivityIntent.putExtra(EditCardActivity.CARD_ID_EXTRA, card.cardId)

        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(4.dp)
                .combinedClickable(
                    onClick = {
                        activity.launch(editTermActivityIntent)
                    },
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.toggleCardSelection(card)
                    })
        ) {
            ListItem(
                colors = if (viewModel.selectedCards.contains(card))
                    ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                else
                    ListItemDefaults.colors(),
                overlineContent = {
                    Text(
                        text = card.kanaValue,
                        style = MaterialTheme.typography.bodySmall
                    )

                },
                headlineContent = {
                    Text(
                        text = card.kanjiValue,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                supportingContent = {
                    Text(
                        text = card.englishValue,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                trailingContent = { Text((card.difficulty).name) })
        }
    }

    @Composable
    fun NoSelectedItemsNavigationBarActions(viewModel: CardSetViewModel) {
        val addTermActivityIntent = Intent(this, AddCardActivity::class.java)
        addTermActivityIntent.putExtra(AddCardActivity.CARD_SET_ID_EXTRA, viewModel.cardSetId)
        val editCardSetActivityIntent = Intent(this, EditCardSetActivity::class.java)
        editCardSetActivityIntent.putExtra(EditCardSetActivity.CARD_SET_ID_EXTRA, viewModel.cardSetId)
        val activity = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.cardSetChanged()
            }
        }


        IconButton(onClick = {
            activity.launch(editCardSetActivityIntent)
        }) { Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit button") }

        IconButton(onClick = {
            activity.launch(addTermActivityIntent)
        }) { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add button") }
    }

    @Composable
    fun SelectedItemsNavigationBarActions(
        viewModel: CardSetViewModel
    ) {
        IconButton(onClick = {
            viewModel.showDeleteConfirmation()
        }) { Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete button") }
        when {
            viewModel.showDeleteConfirmation ->
                DeleteCardsAlertDialog(
                    cardCount = viewModel.selectedCards.size,
                    onConfirmation = viewModel::deleteSelectedCards,
                    onDismiss = viewModel::hideDeleteConfirmation
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