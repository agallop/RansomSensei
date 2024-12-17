package com.example.ransomsensei

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.data.entity.Difficulty
import com.example.ransomsensei.theme.AppTheme

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            AppTheme {
                val WelcomeActivityIntent = Intent(this, WelcomeActivity::class.java)
                val context = LocalContext.current
                val dataStoreManager = RansomSenseiDataStoreManager(context = context)
                var needToSetHomeActivity = remember { mutableStateOf(false) }
                var scope = rememberCoroutineScope()
                var database: RansomSenseiDatabase = Room.databaseBuilder(
                    context = context,
                    RansomSenseiDatabase::class.java,
                    "ransomSensei"
                ).build()
                var cards = remember { mutableStateListOf<Card>() }
                var isLoading = remember { mutableStateOf(true) }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet{ Text("Things go here") }
                    },
                ) {

                LaunchedEffect(key1 = Unit) {
                    needToSetHomeActivity.value = dataStoreManager.getHomeActivity().isEmpty()
                    cards.addAll(database.cardDao().getAll())
                    isLoading.value = false
                }



                Scaffold (
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = { Text("Add") },
                            icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    },

                    topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text("Ransom Sensei")
                        },
                        actions = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) { Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu button") }
                        }
                    )
                }) { padding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(cards) { it -> CardItem(it) }

                        item {
                            Button(onClick = {
                                scope.launch {
                                    dataStoreManager.clearData()
                                    database.clearAllTables()
                                    needToSetHomeActivity.value = true
                                }
                            }) { Text("Clear all data") }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        if (needToSetHomeActivity.value) {
                            needToSetHomeActivity.value = false
                            startActivity(WelcomeActivityIntent)
                        }
                    }
                }
                }
            }
        }
    }

    @Composable
    fun CardItem(card: Card) {
        androidx.compose.material3.Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(4.dp)) {
            ListItem(
                overlineContent = {
                    Text(text = card.kanaValue ?: "",
                        style = MaterialTheme.typography.bodySmall)
                },
                headlineContent = {
                    Text(text = card.kanjiValue ?: "",
                        style = MaterialTheme.typography.bodyLarge)
                },
                supportingContent = {
                    Text(text = card.englishValue ?: "",
                        style = MaterialTheme.typography.bodyMedium)
                },
                trailingContent = { Text((card.difficulty ?: Difficulty.EASY).name) })
        }
    }
}