package org.ransomsensei.activity_onboarding.util

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable object StartOnBoardingScreen : Destination()
    @Serializable object SetHomeActivityScreen : Destination()
    @Serializable object SetDefaultHomeAppScreen : Destination()
    @Serializable object Finish : Destination()
}

