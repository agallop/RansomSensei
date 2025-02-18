package com.example.ransomsensei.ui.activity_main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ransomsensei.data.entity.Difficulty
import com.example.ransomsensei.viewmodel.activity_main.AddEditCardScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCardScreen(navController: NavHostController, viewModel: AddEditCardScreenViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Add new term")
                },
                navigationIcon = {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                keyboardOptions = KeyboardOptions.Default.copy(
                    hintLocales = LocaleList("ja")
                ),
                modifier = Modifier.padding(10.dp),
                label = { Text(text = "Kanji value") },
                value = viewModel.kanjiValue,
                onValueChange = viewModel::updateKanjiValue
            )

            TextField(
                keyboardOptions = KeyboardOptions.Default.copy(
                    hintLocales = LocaleList("ja")
                ),
                modifier = Modifier.padding(10.dp),
                label = { Text(text = "Kana value") },
                value = viewModel.kanaValue,
                onValueChange = viewModel::updateKanaValue
            )

            TextField(
                keyboardOptions = KeyboardOptions.Default.copy(
                    hintLocales = LocaleList("en")
                ),
                modifier = Modifier.padding(10.dp),
                label = { Text(text = "English value") },
                value = viewModel.englishValue,
                onValueChange = viewModel::updateEnglishValue
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = viewModel.difficulty == Difficulty.EASY,
                    onClick = { viewModel.updateDifficulty(Difficulty.EASY) })
                Text("Easy")
                RadioButton(
                    selected = viewModel.difficulty == Difficulty.MEDIUM,
                    onClick = { viewModel.updateDifficulty(Difficulty.MEDIUM) })
                Text("Medium")
                RadioButton(
                    selected = viewModel.difficulty == Difficulty.HARD,
                    onClick = { viewModel.updateDifficulty(Difficulty.HARD) })
                Text("Hard")
            }

            Row {
                Button(
                    content = { Text(text = "Add") },
                    enabled = viewModel.canSave(),
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.insertCard()
                            withContext(Dispatchers.Main) {
                                navController.popBackStack()
                            }
                        }
                    })
            }
        }
    }
}
