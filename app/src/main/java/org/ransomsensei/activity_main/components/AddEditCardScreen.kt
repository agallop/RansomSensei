package org.ransomsensei.activity_main.components

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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ransomsensei.activity_main.viewmodels.AddEditCardViewModel
import org.ransomsensei.data.entity.Difficulty
import org.ransomsensei.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCardScreen(navController: NavHostController, viewModel: AddEditCardViewModel) {
    AddEditCardScreen(
        kanjiValue = viewModel.kanjiValue,
        kanaValue = viewModel.kanaValue,
        englishValue = viewModel.englishValue,
        difficulty = viewModel.difficulty,
        canSave = viewModel.canSave(),
        insertCard = viewModel::insertCard,
        popBackStack = navController::popBackStack,
        updateDifficulty = viewModel::updateDifficulty,
        updateKanaValue = viewModel::updateKanaValue,
        updateKanjiValue = viewModel::updateKanjiValue,
        updateEnglishValue = viewModel::updateEnglishValue
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCardScreen(
    kanjiValue: String,
    kanaValue: String,
    englishValue: String,
    difficulty: Difficulty,
    updateKanjiValue: (String) -> Unit = {},
    updateKanaValue: (String) -> Unit = {},
    updateEnglishValue: (String) -> Unit = {},
    updateDifficulty: (Difficulty) -> Unit = {},
    canSave: Boolean,
    insertCard: suspend () -> Unit,
    popBackStack: () -> Unit
) {
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
                    IconButton(onClick = popBackStack) {
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
                value = kanjiValue,
                onValueChange = updateKanjiValue
            )

            TextField(
                keyboardOptions = KeyboardOptions.Default.copy(
                    hintLocales = LocaleList("ja")
                ),
                modifier = Modifier.padding(10.dp),
                label = { Text(text = "Kana value") },
                value = kanaValue,
                onValueChange = updateKanaValue
            )

            TextField(
                keyboardOptions = KeyboardOptions.Default.copy(
                    hintLocales = LocaleList("en")
                ),
                modifier = Modifier.padding(10.dp),
                label = { Text(text = "English value") },
                value = englishValue,
                onValueChange = updateEnglishValue
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = difficulty == Difficulty.EASY,
                    onClick = { updateDifficulty(Difficulty.EASY) })
                Text("Easy")
                RadioButton(
                    selected = difficulty == Difficulty.MEDIUM,
                    onClick = { updateDifficulty(Difficulty.MEDIUM) })
                Text("Medium")
                RadioButton(
                    selected = difficulty == Difficulty.HARD,
                    onClick = { updateDifficulty(Difficulty.HARD) })
                Text("Hard")
            }

            Row {
                Button(
                    content = { Text(text = "Add") },
                    enabled = canSave,
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            insertCard
                            withContext(Dispatchers.Main) {
                                popBackStack()
                            }
                        }
                    })
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AddEditCardScreenPreview() {
    AppTheme {
        AddEditCardScreen(
            kanjiValue = "初めて",
            kanaValue = "はじめて",
            englishValue = "For the first time",
            difficulty = Difficulty.EASY,
            canSave = true,
            insertCard = {},
            popBackStack = {},
            updateDifficulty = {},
            updateKanaValue = {},
            updateKanjiValue = {},
        )
    }
}
