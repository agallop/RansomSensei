package com.example.ransomsensei.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.viewmodel.LockScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import kotlin.text.isNotEmpty

class LockScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = koinViewModel<LockScreenViewModel>()
            LockScreen(viewModel)
        }
    }

    @Composable
    fun LockScreen(viewModel: LockScreenViewModel) {
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
                homeActivityPackage = viewModel.homeActivityPackage
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
        updateLastInteraction: suspend () -> Unit,
        loadQuestion: () -> Unit
    ) {
        Scaffold { padding ->
            LaunchedEffect(key1 = Unit) {
                loadQuestion()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
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
                        text = "Translate to gain entry:", style = MaterialTheme.typography.titleLarge
                    )
                }
                AnimatedVisibility(visible = !isLoading) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (showQuestion) {
                            BasicQuestion(
                                kanaValue = kanaValue,
                                kanjiValue = kanjiValue,
                                englishValue = englishValue,
                                allowSkip = allowSkip,
                                currentAnswer = currentAnswer,
                                currentCountDown = currentCountDown,
                                onCurrentAnswerChange = onCurrentAnswerChange,
                                isLoading = isLoading
                            ) {
                                val intent = Intent(Intent.ACTION_MAIN)
                                intent.addCategory(Intent.CATEGORY_HOME)
                                CoroutineScope(Dispatchers.IO).launch {
                                    updateLastInteraction()
                                    withContext(Dispatchers.Main) {
                                        if (homeActivityPackage.isNotEmpty()) {
                                            intent.setPackage(homeActivityPackage)
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            val chooser =
                                                Intent.createChooser(intent, /* title */ null)
                                            startActivity(chooser)
                                            finish()
                                        }
                                    }
                                }
                            }
                        } else {
                            Button(content = { Text("Proceed") }, onClick = {
                                val intent = Intent(Intent.ACTION_MAIN)
                                intent.addCategory(Intent.CATEGORY_HOME)
                                if (homeActivityPackage.isNotEmpty()) {
                                    intent.setPackage(homeActivityPackage)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                } else {
                                    val chooser = Intent.createChooser(intent, /* title */ null)
                                    startActivity(chooser)
                                }
                                finish()
                            })
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun BasicQuestion(
        kanaValue: String,
        kanjiValue: String,
        englishValue: String,
        currentAnswer: String,
        allowSkip: Boolean,
        isLoading: Boolean,
        currentCountDown: Flow<String>,
        onCurrentAnswerChange: (String) -> Unit,
        onCompletion: () -> Unit
    ) {

        Card(
            modifier = Modifier
                .padding(16.dp)
                .width(240.dp)
                .height(80.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Text(kanjiValue)
                }
                Row {
                    Text(kanaValue)
                }
            }
        }

        TextField(
            value = currentAnswer, onValueChange = onCurrentAnswerChange
        )

        Button(onClick = {
            if (currentAnswer.toLowerCase(Locale.current) == (englishValue.toLowerCase(Locale.current))) {
                onCompletion()
            }
        }) { Text("Check my answer") }
        Surface(onClick = onCompletion, enabled = allowSkip) {
            if (allowSkip) {
                Text("Skip for now")
            } else {
                Text(text = currentCountDown.collectAsState("").value)
            }
        }
    }

    @Preview
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
                allowSkip = false,
                currentCountDown = flow { emit("10s") })
        }
    }

    @Preview
    @Composable
    fun DarkModeLockScreenPreview() {
        AppTheme(darkTheme = true) {
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
                currentCountDown = flow {})
        }
    }
}