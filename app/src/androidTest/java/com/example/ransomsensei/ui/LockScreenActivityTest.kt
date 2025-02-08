package com.example.ransomsensei.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** Compose Ui test for [LockScreenActivity] */
class LockScreenActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<LockScreenActivity>()

    @Before
    fun waitForAnimation() {
        composeTestRule.waitUntil(timeoutMillis = 5000L) {
            composeTestRule.onNodeWithTag("AnimatedVisibility").isDisplayed()
        }
    }

    @Test
    fun initialState() {
        composeTestRule.onNodeWithText("日本語").assertIsDisplayed()
        composeTestRule.onNodeWithText("にほんご").assertIsDisplayed()
        composeTestRule.onNodeWithText("Check my answer").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AnswerTextField").assertTextEquals("")
        composeTestRule.onNodeWithText("Skip for now").isNotDisplayed()
    }

    @Test
    fun inputWrongAnswer() {
        composeTestRule.onNodeWithTag("AnswerTextField").performTextInput("English")
        composeTestRule.onNodeWithText("Check my answer").performClick()

        composeTestRule.onNodeWithText("日本語").assertIsDisplayed()
        composeTestRule.onNodeWithText("にほんご").assertIsDisplayed()
        composeTestRule.onNodeWithText("Check my answer").assertIsDisplayed()
    }

    @Test
    fun inputCorrectAnswer() {
        composeTestRule.onNodeWithTag("AnswerTextField").performTextInput("Japanese")
        composeTestRule.onNodeWithText("Check my answer").performClick()

        composeTestRule.onNodeWithText("日本語").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("にほんご").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Check my answer").assertIsNotDisplayed()
    }

    @Test
    fun skip() {
        composeTestRule.waitUntil(timeoutMillis = 6000L) {
            composeTestRule.onNodeWithText("Skip for now").isDisplayed()
        }
        composeTestRule.onNodeWithText("Skip for now").performClick()

        composeTestRule.onNodeWithText("日本語").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("にほんご").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Check my answer").assertIsNotDisplayed()
    }
}