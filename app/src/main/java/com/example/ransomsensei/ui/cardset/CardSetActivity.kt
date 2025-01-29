package com.example.ransomsensei.ui.cardset

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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.WelcomeActivity
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.ui.AddTermActivity
import com.example.ransomsensei.ui.EditTermActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CardSetActivity : ComponentActivity() {

    companion object {
        const val CARD_SET_ID_EXTRA = "CARD_SET_ID"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cardSetId = intent.getIntExtra(CARD_SET_ID_EXTRA, 0)

    setContent {
        AppTheme {
            val welcomeActivityIntent = Intent(this, WelcomeActivity::class.java)

            val context = LocalContext.current
            val dataStoreManager = RansomSenseiDataStoreManager(context = context)
            val needToSetHomeActivity = remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            val database = RansomSenseiDatabase.getInstance(context)
            val cardSet = remember { mutableStateOf(CardSet.getDefaultInstance()) }
            val cards = remember { mutableStateOf<List<Card>>(listOf()) }
            val isLoading = remember { mutableStateOf(true) }
            val selectedCards = remember { mutableStateMapOf<Card, Boolean>() }
            val hasChanges = remember { mutableStateOf(false) }

            LaunchedEffect(key1 = Unit) {
                needToSetHomeActivity.value = dataStoreManager.getHomeActivity().isEmpty()
                cards.value = database.cardDao().getCardsInSet(cardSetId = cardSetId)
                cardSet.value = database.cardSetDao().getCardSet(cardSetId)
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
                            Text(cardSet.value.cardSetName)
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                if(hasChanges.value)
                                    setResult(RESULT_OK)
                                finish()}) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
                            }
                        },
                        actions =
                        {
                            if (selectedCards.isEmpty()) NoSelectedItemsNavigationBarActions(cardSetId) {
                                hasChanges.value = true
                                scope.launch {
                                    cards.value = database.cardDao().getCardsInSet(cardSetId = cardSetId)
                                    cardSet.value = database.cardSetDao().getCardSet(cardSetId)
                                }
                                selectedCards.clear()
                            }
                            else SelectedItemsNavigationBarActions(
                                selectedCards.keys,
                                scope,
                                database
                            ) {
                                scope.launch {
                                    hasChanges.value = true
                                    cards.value = database.cardDao().getCardsInSet(cardSetId = cardSetId)
                                }
                                selectedCards.clear()
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
                    items(cards.value) {
                        CardItem(
                            card = it,
                            selected = selectedCards.contains(it),
                            onSelectionChange = { selected ->
                                if (selected) selectedCards[it] = true else selectedCards.remove(it)
                            },
                            onEdit = {
                                scope.launch {
                                    cards.value = cards.value.map { oldCard ->
                                        if (oldCard.cardId == it.cardId)
                                            database.cardDao().getCard(cardId = it.cardId)
                                        else oldCard
                                    }
                                }
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
    }}

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CardItem(
        card: Card,
        selected: Boolean = false,
        onSelectionChange: (Boolean) -> Unit,
        onEdit: () -> Unit
    ) {

        val activity = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                onEdit()
            }
        }
        val haptics = LocalHapticFeedback.current
        val editTermActivityIntent = Intent(this, EditTermActivity::class.java)
        editTermActivityIntent.putExtra(EditTermActivity.CARD_ID_EXTRA, card.cardId)

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
    fun NoSelectedItemsNavigationBarActions(cardSetId: Int, onChange: () -> Unit) {
        val addTermActivityIntent = Intent(this, AddTermActivity::class.java)
        addTermActivityIntent.putExtra(AddTermActivity.CARD_SET_ID_EXTRA, cardSetId)
        val editCardSetActivityIntent = Intent(this, EditCardSetActivity::class.java)
        editCardSetActivityIntent.putExtra(EditCardSetActivity.CARD_SET_ID_EXTRA, cardSetId)
        val activity = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                onChange()
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
        cards: Set<Card>,
        scope: CoroutineScope,
        database: RansomSenseiDatabase,
        onEdit: () -> Unit
    ) {
        val showDeleteConfirmation = remember { mutableStateOf(false) }
        val editTermActivityIntent = Intent(this, EditTermActivity::class.java)


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
        when {
            showDeleteConfirmation.value ->
                DeleteCardsAlertDialog(
                    cardCount = cards.size,
                    onConfirmation = {
                        scope.launch {
                            database.cardDao().deleteCards(cards)
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