/*
 *  Copyright (c) 2025 Anthony Gallop <agallopdev@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
        updateEnglishValue = viewModel::updateEnglishValue,
        isNew = viewModel.isNew,
        clearForm = viewModel::clearForm
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
    popBackStack: () -> Unit,
    isNew: Boolean,
    clearForm: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
                            contentDescription = "Back"
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
                    content = { Text(text = if (isNew) "Add" else "Update") },
                    enabled = canSave,
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            insertCard()
                            withContext(Dispatchers.Main) {
                                if (isNew) {
                                    snackbarHostState.showSnackbar(
                                        "「${
                                            if (kanjiValue.isNotEmpty())
                                                kanjiValue
                                            else kanaValue
                                        }」 Added!"
                                    )
                                    clearForm()
                                } else {
                                    popBackStack()
                                }
                            }
                        }
                    })
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AddEditCardScreenPreview_Add() {
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
            isNew = true,
            clearForm = {}
        )
    }
}

@PreviewLightDark
@Composable
fun AddEditCardScreenPreview_Edit() {
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
            isNew = false,
            clearForm = {}
        )
    }
}
