package com.example.ransomsensei.ui.activity_main.util

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable object CardSetsScreen : Destination
}