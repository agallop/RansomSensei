package org.ransomsensei.activity_settings.util

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    object Finish : Destination()
    @Serializable
    object SettingsScreen : Destination()
}