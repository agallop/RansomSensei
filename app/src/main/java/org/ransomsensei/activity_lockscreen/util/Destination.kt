package org.ransomsensei.activity_lockscreen.util

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable object ReturnToOnboardingScreen : Destination()
    @Serializable object Onboarding : Destination()
    @Serializable object LockScreen : Destination()
    @Serializable data class Finish(val homePackage: String) : Destination()
}