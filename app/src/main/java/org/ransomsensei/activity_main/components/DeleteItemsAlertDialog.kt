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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun DeleteItemsAlertDialog(
    itemCount: Int,
    singleItemLabel: String,
    multipleItemLabel: String,
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
                text = if (itemCount == 1)
                    "Are you sure you want to delete this $singleItemLabel?"
                else
                    "Are you sure you want to delete these $itemCount $multipleItemLabel?"
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

@Preview
@Composable
fun DeleteItemsAlertDialogPreview() {
    DeleteItemsAlertDialog(
        itemCount = 5,
        singleItemLabel = "card set",
        multipleItemLabel = "card sets",
        onConfirmation = {},
        onDismiss = {}
    )
}

