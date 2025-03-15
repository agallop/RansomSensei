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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ransomsensei.activity_main.viewmodels.AddEditCardSetViewModel
import org.ransomsensei.data.entity.CardSetStatus
import org.ransomsensei.theme.AppTheme

@Composable
fun AddEditCardSetScreen(
    navHostController: NavHostController, viewModel: AddEditCardSetViewModel
) {
    AddEditCardSetScreen(
        isNew = viewModel.isNew,
        name = viewModel.name,
        status = viewModel.status,
        onNameChange = viewModel::onNameChange,
        onStatusChange = viewModel::onStatusChange,
        insertOrUpdateCardSet = viewModel::insertOrUpdateCardSet,
        popBackStack = navHostController::popBackStack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCardSetScreen(
    isNew: Boolean,
    name: String,
    status: CardSetStatus,
    onNameChange: (String) -> Unit,
    onStatusChange: (CardSetStatus) -> Unit,
    insertOrUpdateCardSet: suspend () -> Unit,
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
                    Text(if (isNew) "Add set" else "Edit Set")
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
                modifier = Modifier.padding(10.dp),
                label = { Text(text = "Set name") },
                value = name,
                onValueChange = onNameChange
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = status == CardSetStatus.ENABLED,
                    onClick = { onStatusChange(CardSetStatus.ENABLED) })
                Text("Active")
                RadioButton(
                    selected = status == CardSetStatus.DISABLED,
                    onClick = { onStatusChange(CardSetStatus.DISABLED) })
                Text("Inactive")
            }

            Row {
                Button(
                    content = { Text(text = "Done") },
                    enabled = name.isNotEmpty()
                            && status != CardSetStatus.UNKNOWN,
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            insertOrUpdateCardSet()
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
fun AddEditCardSetScreenPreview() {
    AppTheme {
        AddEditCardSetScreen(
            isNew = true,
            name = "Days of the Week",
            status = CardSetStatus.ENABLED,
            onNameChange = {},
            onStatusChange = {},
            insertOrUpdateCardSet = {},
            popBackStack = {}
        )
    }
}
