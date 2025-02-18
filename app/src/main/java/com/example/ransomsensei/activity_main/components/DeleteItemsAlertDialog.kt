package com.example.ransomsensei.activity_main.components

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

