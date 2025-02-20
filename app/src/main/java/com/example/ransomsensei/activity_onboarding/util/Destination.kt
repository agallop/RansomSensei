package com.example.ransomsensei.activity_onboarding.util

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable object SetHomeActivityScreen : Destination()
    @Serializable object SetDefaultHomeAppScreen : Destination()
    @Serializable object Finish : Destination()
}