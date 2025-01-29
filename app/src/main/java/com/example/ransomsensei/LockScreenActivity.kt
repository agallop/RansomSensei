package com.example.ransomsensei

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.viewmodel.LockScreenViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.text.isNotEmpty

class LockScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val viewModel = koinViewModel<LockScreenViewModel>()

                Scaffold {  padding ->

                LaunchedEffect(key1 = Unit) {
                    viewModel.loadQuestion()
                }

                Column(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if(!viewModel.isLoading) {
                        if (viewModel.showQuestion) {
                            BasicQuestion(viewModel) {
                                val intent = Intent(Intent.ACTION_MAIN)
                                intent.addCategory(Intent.CATEGORY_HOME)
                                if (viewModel.homeActivityPackage.isNotEmpty()) {
                                    intent.setPackage(viewModel.homeActivityPackage)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                } else {
                                    val chooser = Intent.createChooser(intent, /* title */ null)
                                    startActivity(chooser)
                                }
                                viewModel.updateLastInteraction()
                            }
                        } else {
                            Button(content = {Text("Proceed")}, onClick = {
                                val intent = Intent(Intent.ACTION_MAIN)
                                intent.addCategory(Intent.CATEGORY_HOME)
                                if (viewModel.homeActivityPackage.isNotEmpty()) {
                                    intent.setPackage(viewModel.homeActivityPackage)
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
    }

    @Composable
    fun BasicQuestion(viewModel: LockScreenViewModel, onCompletion: () -> Unit) {
            Row {
                Text(viewModel.card!!.kanjiValue)
            }
            Row {
                Text(viewModel.card!!.kanaValue)
            }

            TextField(
                value = viewModel.currentAnswer,
                onValueChange = { viewModel.setAnswer(it) })

            Button(onClick = {
                if (viewModel.currentAnswer.toLowerCase(Locale.current) ==
                    (viewModel.card!!.englishValue.toLowerCase(Locale.current))) {
                    onCompletion()
                }
            }) { Text("Check my answer") }
        }
}