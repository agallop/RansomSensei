package org.ransomsensei.activity_main.util

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable object CardSetsScreen : Destination
    @Serializable class AddEditCardSetScreen(val cardSetId: Int? = null): Destination
    @Serializable class CardSetDetailsScreen(val cardSetId: Int): Destination
    @Serializable class AddEditCardScreen(val cardSetId: Int, val cardId: Int? = null): Destination
}