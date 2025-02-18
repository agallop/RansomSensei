package com.example.ransomsensei.ui.activity_main.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.ui.activity_main.util.Destination
import com.example.ransomsensei.viewmodel.activity_main.CardSetDetailsScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSetDetailsScreen(
    navHostController: NavHostController,
    viewModel: CardSetDetailsScreenViewModel
) {
    AppTheme {
        val cards = viewModel.cards.collectAsState().value

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(viewModel.cardSet.collectAsState(CardSet.getDefaultInstance()).value.cardSetName)
                    },
                    navigationIcon = {
                        IconButton(
                            onClick =
                            navHostController::popBackStack
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back button"
                            )
                        }
                    },
                    actions =
                    {
                        if (viewModel.selectedCards.isEmpty())
                            NoSelectedItemsNavigationBarActions(
                                viewModel.cardSetId,
                                navHostController
                            )
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
                itemsIndexed(cards) { index, card ->
                    CardItem(
                        card = card,
                        viewModel,
                        navHostController
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardItem(
    card: Card,
    viewModel: CardSetDetailsScreenViewModel,
    navHostController: NavHostController

) {
    val haptics = LocalHapticFeedback.current

    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = {
                    navHostController.navigate(
                        Destination.AddEditCardScreen(
                            cardSetId = card.cardSetId,
                            cardId = card.cardId
                        )
                    )
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
fun NoSelectedItemsNavigationBarActions(cardSetId: Int, navHostController: NavHostController) {
    IconButton(onClick = {
        navHostController.navigate(Destination.AddEditCardSetScreen(cardSetId = cardSetId))
    }) { Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit button") }

    IconButton(onClick = {
        navHostController.navigate(Destination.AddEditCardScreen(cardSetId = cardSetId))

    }) { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add button") }
}

@Composable
fun SelectedItemsNavigationBarActions(
    viewModel: CardSetDetailsScreenViewModel
) {
    IconButton(onClick = {
        viewModel.showDeleteConfirmation()
    }) { Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete button") }
    when {
        viewModel.showDeleteConfirmation ->
            DeleteItemsAlertDialog(
                itemCount = viewModel.selectedCards.size,
                singleItemLabel = "card",
                multipleItemLabel = "cards",
                onConfirmation = viewModel::deleteSelectedCards,
                onDismiss = viewModel::hideDeleteConfirmation
            )
    }
}

