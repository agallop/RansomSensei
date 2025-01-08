package com.example.ransomsensei

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.ui.cardset.AddCardSetActivity
import com.example.ransomsensei.ui.cardset.CardSetActivity
import com.example.ransomsensei.ui.cardset.EditCardSetActivity
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val welcomeActivityIntent = Intent(this, WelcomeActivity::class.java)

                val context = LocalContext.current
                val dataStoreManager = RansomSenseiDataStoreManager(context = context)
                val needToSetHomeActivity = remember { mutableStateOf(false) }
                val scope = rememberCoroutineScope()
                val database = RansomSenseiDatabase.getInstance(context)
                val cardSets = remember { mutableStateOf<List<CardSet>>(listOf()) }
                val isLoading = remember { mutableStateOf(true) }
                val selectedCardSets = remember { mutableStateMapOf<CardSet, Boolean>() }

                LaunchedEffect(key1 = Unit) {
                    needToSetHomeActivity.value = dataStoreManager.getHomeActivity().isEmpty()
                    cardSets.value = database.cardSetDao().getAll()
                    isLoading.value = false
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
                                if (selectedCardSets.isEmpty()) NoSelectedItemsNavigationBarActions {
                                    scope.launch {
                                        cardSets.value = database.cardSetDao().getAll()
                                    }
                                    selectedCardSets.clear()
                                }
                                else SelectedItemsNavigationBarActions(
                                    selectedCardSets.keys,
                                    scope,
                                    database
                                ) {
                                    scope.launch {
                                        cardSets.value = database.cardSetDao().getAll()
                                    }
                                    selectedCardSets.clear()
                                }
                            }

                        )
                    }) { padding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(cardSets.value) {
                            CardSetCard(
                                cardSet = it,
                                selected = selectedCardSets.contains(it),
                                onSelectionChange = { selected ->
                                    if (selected) selectedCardSets.put(
                                        it,
                                        true
                                    ) else selectedCardSets.remove(it)
                                }
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        if (needToSetHomeActivity.value) {
                            needToSetHomeActivity.value = false
                            startActivity(welcomeActivityIntent)
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CardSetCard(
        cardSet: CardSet,
        selected: Boolean = false,
        onSelectionChange: (Boolean) -> Unit
    ) {
        val haptics = LocalHapticFeedback.current

        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(4.dp)
                .combinedClickable(
                    onClick = {
                        val cardSetIntent = Intent(this, CardSetActivity::class.java)
                        cardSetIntent.putExtra(CardSetActivity.CARD_SET_ID_EXTRA, cardSet.cardSetId)
                        startActivity(cardSetIntent)
                    },
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSelectionChange(!selected)
                    })
        ) {

            ListItem(
                colors = if (!selected)
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
    fun NoSelectedItemsNavigationBarActions(onEdit: () -> Unit) {
        val addCardSetIntent = Intent(this, AddCardSetActivity::class.java)
        val activity = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                onEdit()
            }
        }

        IconButton(onClick = {
            activity.launch(addCardSetIntent)
        }) { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add button") }
    }

    @Composable
    fun SelectedItemsNavigationBarActions(
        cardSets: Set<CardSet>,
        scope: CoroutineScope,
        database: RansomSenseiDatabase,
        onEdit: () -> Unit
    ) {
        val showDeleteConfirmation = remember { mutableStateOf(false) }
        val editCardSetActivity = Intent(this, EditCardSetActivity::class.java)


        val activity = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                onEdit()
            }
        }

        IconButton(onClick = {
            showDeleteConfirmation.value = true
        }) { Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete button") }
        IconButton(
            enabled = cardSets.size == 1,
            onClick = {
                editCardSetActivity.putExtra(
                    EditCardSetActivity.CARD_SET_ID_EXTRA,
                    cardSets.single().cardSetId
                )
                activity.launch(editCardSetActivity)

            }) { Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit button") }

        when {
            showDeleteConfirmation.value ->
                DeleteCardsAlertDialog(
                    cardCount = cardSets.size,
                    onConfirmation = {
                        scope.launch {
                            database.cardSetDao().deleteCardSets(cardSets)
                            showDeleteConfirmation.value = false
                            onEdit()
                        }
                    },
                    onDismiss = { showDeleteConfirmation.value = false }
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