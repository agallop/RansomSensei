package com.example.ransomsensei.ui

import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

/** Compose Ui test for [LockScreenActivity] */
class LockScreenActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<LockScreenActivity>()

    @Test
    fun questionDisplayedAfterDelay() {
        composeTestRule.onNodeWithText("日本語").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("にほんご").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Check my answer").assertIsNotDisplayed()

        composeTestRule.waitUntil(timeoutMillis = 5000L) {
            composeTestRule.onNodeWithText("日本語").isDisplayed() &&
                    composeTestRule.onNodeWithText("にほんご").isDisplayed() &&
                    composeTestRule.onNodeWithText("Check my answer").isDisplayed()
        }
    }
}