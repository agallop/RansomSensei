package com.example.ransomsensei.activity_main.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
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
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.activity_main.util.Destination
import com.example.ransomsensei.activity_main.viewmodels.CardSetsViewModel

@Composable
fun CardSetsScreen(navigationController: NavHostController, viewModel: CardSetsViewModel) {
    CardSetsScreen(
        navigate = { navigationController.navigate(it) },
        showDeleteConfirmation = viewModel::showDeleteConfirmation,
        hideDeleteConfirmation = viewModel::hideDeleteConfirmation,
        deleteSelectedCardSets = viewModel::deleteSelectedCardSets,
        deleteConfirmationShown = viewModel.showDeleteConfirmation,
        cardSets = viewModel.cardSets.collectAsState().value,
        selectedCardSets = viewModel.selectedCardSets,
        onCardSetLongClick = viewModel::toggleCardSetSelection
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardSetsScreen(
    navigate: (Destination) -> Unit,
    showDeleteConfirmation: () -> Unit,
    hideDeleteConfirmation: () -> Unit,
    deleteSelectedCardSets: () -> Unit,
    deleteConfirmationShown: Boolean,
    cardSets: List<CardSet>,
    selectedCardSets: Set<CardSet>,
    onCardSetLongClick: (CardSet) -> Unit
) {
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
                    if (selectedCardSets.isEmpty()) {
                        CardSetsDefaultNavigationBarActions(navigate = navigate)
                    } else SelectedItemsNavigationBarActions(
                        showDeleteConfirmation = showDeleteConfirmation,
                        hideDeleteConfirmation = hideDeleteConfirmation,
                        deleteSelectedCardSets = deleteSelectedCardSets,
                        deleteConfirmationShown = deleteConfirmationShown,
                        itemCount = selectedCardSets.size,
                        singleItemLabel = "card set",
                        multipleItemLabel = "card sets",
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

            items(cardSets) {
                CardSetCard(
                    navigate = navigate,
                    cardSet = it,
                    isSelected = selectedCardSets.contains(it),
                    onCardSetLongClick = onCardSetLongClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardSetCard(
    navigate: (Destination) -> Unit,
    cardSet: CardSet,
    isSelected: Boolean,
    onCardSetLongClick: (CardSet) -> Unit,
) {
    val haptics = LocalHapticFeedback.current

    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = {
                    navigate(Destination.CardSetDetailsScreen(cardSet.cardSetId))
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onCardSetLongClick(cardSet)
                })
    ) {
        ListItem(
            colors = if (!isSelected)
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
fun CardSetsDefaultNavigationBarActions(navigate: (Destination) -> Unit) {
    IconButton(onClick = {
       navigate(Destination.AddEditCardSetScreen())
    }) { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add button") }
}




@PreviewLightDark
@Composable
fun CardSetsScreenPreview() {
    AppTheme {
        CardSetsScreen(
            navigate = {},
            showDeleteConfirmation = {},
            hideDeleteConfirmation = {},
            deleteSelectedCardSets = {},
            deleteConfirmationShown = false,
            cardSets = listOf(
                CardSet(
                    cardSetName = "Days of the Week",
                    cardCount = 7,
                    cardSetStatus = CardSetStatus.ENABLED
                ),
                CardSet(
                    cardSetName = "Greetings",
                    cardCount = 5,
                    cardSetStatus = CardSetStatus.DISABLED
                ),
            ),
            selectedCardSets = setOf(),
            onCardSetLongClick = {})
    }
}