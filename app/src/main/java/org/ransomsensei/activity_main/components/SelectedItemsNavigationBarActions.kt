package org.ransomsensei.activity_main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun SelectedItemsNavigationBarActions(
    showDeleteConfirmation: () -> Unit,
    hideDeleteConfirmation: () -> Unit,
    deleteSelectedCardSets: () -> Unit,
    deleteConfirmationShown: Boolean,
    itemCount: Int,
    singleItemLabel: String,
    multipleItemLabel: String,
) {
    IconButton(onClick = showDeleteConfirmation) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete button"
        )
    }
    when {
        deleteConfirmationShown ->
            DeleteItemsAlertDialog(
                itemCount = itemCount,
                singleItemLabel = singleItemLabel,
                multipleItemLabel = multipleItemLabel,
                onConfirmation = deleteSelectedCardSets,
                onDismiss = hideDeleteConfirmation
            )
    }
}