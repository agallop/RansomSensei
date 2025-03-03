package org.ransomsensei.activity_lockscreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ransomsensei.activity_lockscreen.viewmodels.LockScreenViewModel
import org.ransomsensei.activity_lockscreen.util.Destination
import org.ransomsensei.theme.AppTheme

@Composable
fun LockScreen(viewModel: LockScreenViewModel, navHostController: NavHostController) {
    AppTheme {
        LockScreen(
            isLoading = viewModel.isLoading,
            showQuestion = viewModel.showQuestion,
            kanaValue = viewModel.card?.kanaValue ?: "",
            kanjiValue = viewModel.card?.kanjiValue ?: "",
            englishValue = viewModel.card?.englishValue ?: "",
            currentAnswer = viewModel.currentAnswer,
            currentCountDown = viewModel.countdown,
            allowSkip = viewModel.allowSkip,
            onCurrentAnswerChange = viewModel::setAnswer,
            loadQuestion = viewModel::loadQuestion,
            updateLastInteraction = viewModel::updateLastInteraction,
            homeActivityPackage = viewModel.homeActivityPackage,
            navigate = navHostController::navigate
        )
    }
}

@Composable
fun LockScreen(
    isLoading: Boolean,
    showQuestion: Boolean,
    kanaValue: String,
    kanjiValue: String,
    englishValue: String,
    currentAnswer: String,
    currentCountDown: Flow<String>,
    allowSkip: Boolean,
    onCurrentAnswerChange: (String) -> Unit,
    homeActivityPackage: String,
    updateLastInteraction: () -> Unit,
    loadQuestion: () -> Unit,
    navigate: (Destination) -> Unit
) {
    Scaffold { padding ->
        LaunchedEffect(key1 = Unit) {
            loadQuestion()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                modifier = Modifier.testTag("AnimatedVisibility"),
                visible = isLoading || showQuestion
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = "Lock",
                        Modifier
                            .padding(vertical = 16.dp)
                            .size(64.dp)
                    )

                    Row {
                        Text(
                            text = "Translate to gain entry",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
            AnimatedVisibility(
                modifier = Modifier.testTag("AnimatedVisibility"),
                visible = !isLoading
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (showQuestion) {
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .width(225.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row {
                                    Text(
                                        text = AnnotatedString(
                                            kanjiValue,
                                            spanStyle = SpanStyle(
                                                localeList = LocaleList("ja")
                                            ),
                                            paragraphStyle = ParagraphStyle(
                                                lineBreak = LineBreak.Heading
                                            )
                                        ),
                                    )
                                }

                                Row {
                                    Text(
                                        text = AnnotatedString(
                                            kanaValue,
                                            spanStyle = SpanStyle(
                                                localeList = LocaleList("ja")
                                            ),
                                            paragraphStyle = ParagraphStyle(
                                                lineBreak = LineBreak.Heading
                                            )
                                        )
                                    )
                                }
                            }
                        }

                        TextField(
                            modifier = Modifier.testTag("AnswerTextField"),
                            value = currentAnswer, onValueChange = onCurrentAnswerChange
                        )

                        Button(onClick = {
                            if (currentAnswer.toLowerCase(Locale.current) ==
                                (englishValue.toLowerCase(Locale.current))
                            ) {
                                onCorrectAnswer(
                                    homeActivityPackage,
                                    updateLastInteraction,
                                    navigate
                                )
                            }
                        }) { Text("Check my answer") }
                        Surface(
                            onClick = { onSkip(homeActivityPackage, navigate) },
                            enabled = allowSkip
                        ) {
                            if (allowSkip) {
                                Text("Skip for now")
                            } else {
                                Text(text = currentCountDown.collectAsState("").value)
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .width(225.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = "No available terms found. Create some, or proceed with using your phone"
                            )
                        }
                        Button(onClick = {
                            navigate(Destination.Main)
                        }) { Text("Edit your card sets") }
                        Button(onClick = {
                            onSkip(homeActivityPackage, navigate)
                        }) { Text("Proceed to home screen") }
                    }
                }
            }
        }
    }
}

private fun onCorrectAnswer(
    homeActivityPackage: String,
    updateLastInteraction: () -> Unit,
    navigate: (Destination) -> Unit
) {
    updateLastInteraction()
    navigate(Destination.Finish(homeActivityPackage))
}

private fun onSkip(homeActivityPackage: String, navigate: (Destination) -> Unit) {
    navigate(Destination.Finish(homeActivityPackage))
}


@PreviewLightDark
@Composable
fun LockScreenPreview() {
    AppTheme {
        LockScreen(isLoading = false,
            showQuestion = true,
            kanaValue = "にちようび",
            kanjiValue = "日曜日",
            englishValue = "sunday",
            currentAnswer = "Sunday",
            onCurrentAnswerChange = {},
            loadQuestion = {},
            updateLastInteraction = {},
            homeActivityPackage = "",
            allowSkip = true,
            currentCountDown = flow { },
            navigate = {})
    }
}

@PreviewLightDark
@Composable
fun LockScreenPreview_NoQuestion() {
    AppTheme {
        LockScreen(isLoading = false,
            showQuestion = false,
            kanaValue = "にちようび",
            kanjiValue = "日曜日",
            englishValue = "sunday",
            currentAnswer = "Sunday",
            onCurrentAnswerChange = {},
            loadQuestion = {},
            updateLastInteraction = {},
            homeActivityPackage = "",
            allowSkip = true,
            currentCountDown = flow { },
            navigate = {})
    }
}