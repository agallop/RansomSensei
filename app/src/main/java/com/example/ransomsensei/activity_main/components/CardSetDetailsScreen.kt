package com.example.ransomsensei.activity_main.components

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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.activity_main.util.Destination
import com.example.ransomsensei.activity_main.viewmodels.CardSetDetailsViewModel
import com.example.ransomsensei.data.entity.Difficulty
import com.example.ransomsensei.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSetDetailsScreen(
    navHostController: NavHostController,
    viewModel: CardSetDetailsViewModel
) {
    CardSetDetailsScreen(
        cards = viewModel.cards.collectAsState().value,
        cardSet = viewModel.cardSet.collectAsState().value,
        selectedCards = viewModel.selectedCards,
        popBackStack = navHostController::popBackStack,
        toggleCardSelection = viewModel::toggleCardSelection,
        showDeleteConfirmation = viewModel::showDeleteConfirmation,
        hideDeleteConfirmation = viewModel::hideDeleteConfirmation,
        deleteSelectedCardSets = viewModel::deleteSelectedCards,
        deleteConfirmationShown = viewModel.showDeleteConfirmation,
        navigate = navHostController::navigate
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSetDetailsScreen(
    cards: List<Card>,
    cardSet: CardSet,
    selectedCards: Set<Card>,
    navigate: (Destination) -> Unit,
    toggleCardSelection: (Card) -> Unit,
    popBackStack: () -> Unit,
    showDeleteConfirmation: () -> Unit,
    hideDeleteConfirmation: () -> Unit,
    deleteSelectedCardSets: () -> Unit,
    deleteConfirmationShown: Boolean,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(cardSet.cardSetName)
                },
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions =
                {
                    if (selectedCards.isEmpty())
                        CardSetsDetailsDefaultNavigationBarActions(
                            cardSetId = cardSet.cardSetId,
                            navigate = navigate
                        )
                    else
                        SelectedItemsNavigationBarActions(
                            showDeleteConfirmation = showDeleteConfirmation,
                            hideDeleteConfirmation = hideDeleteConfirmation,
                            deleteSelectedCardSets = deleteSelectedCardSets,
                            deleteConfirmationShown = deleteConfirmationShown,
                            itemCount = selectedCards.size,
                            singleItemLabel = "card",
                            multipleItemLabel = "cards",
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
            itemsIndexed(cards) { index, card ->
                CardItem(
                    card = card,
                    selectedCards = selectedCards,
                    toggleCardSelection = toggleCardSelection,
                    navigate = navigate
                )
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardItem(
    card: Card,
    selectedCards: Set<Card>,
    toggleCardSelection: (Card) -> Unit,
    navigate: (Destination) -> Unit
) {
    val haptics = LocalHapticFeedback.current

    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = {
                    navigate(
                        Destination.AddEditCardScreen(
                            cardSetId = card.cardSetId,
                            cardId = card.cardId
                        )
                    )
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    toggleCardSelection(card)
                })
    ) {
        ListItem(
            colors = if (selectedCards.contains(card))
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
fun CardSetsDetailsDefaultNavigationBarActions(
    cardSetId: Int,
    navigate: (Destination) -> Unit
) {
    IconButton(onClick = {
        navigate(Destination.AddEditCardSetScreen(cardSetId = cardSetId))
    }) { Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit button") }

    IconButton(onClick = {
        navigate(Destination.AddEditCardScreen(cardSetId = cardSetId))

    }) { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add button") }
}

@PreviewLightDark
@Composable
fun CardSetDetailsScreenPreview() {
    AppTheme {
        CardSetDetailsScreen(
            cards = listOf(
                Card(
                    kanjiValue = "日曜日",
                    kanaValue = "にちようび",
                    englishValue = "Sunday",
                    difficulty = Difficulty.EASY,
                ),
                Card(
                    kanjiValue = "月曜日",
                    kanaValue = "げつようび",
                    englishValue = "Monday",
                    difficulty = Difficulty.EASY,
                )
            ),
            cardSet = CardSet(
                cardSetName = "Days of the Week"
            ),
            selectedCards = setOf(),
            navigate = {},
            toggleCardSelection = {},
            popBackStack = {},
            showDeleteConfirmation = {},
            hideDeleteConfirmation = {},
            deleteSelectedCardSets = {},
            deleteConfirmationShown = false
        )
    }
}