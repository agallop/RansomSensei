package com.example.ransomsensei.ui.card

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.data.entity.Difficulty
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.viewmodel.card.AddCardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

class AddCardActivity : ComponentActivity() {

    companion object {
        const val CARD_SET_ID_EXTRA = "CARD_SET_ID_EXTRA"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cardSetId = intent.getIntExtra(CARD_SET_ID_EXTRA, 0)


        setContent {
            val viewModel = koinViewModel<AddCardViewModel>()
            viewModel.updateCardSetId(cardSetId)

            AppTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Add new term")
                            },
                            navigationIcon = {
                                IconButton(onClick = { setResult(RESULT_CANCELED); finish() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back button"
                                    )
                                }
                            })
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        TextField(
                            keyboardOptions = KeyboardOptions.Default.copy(
                                hintLocales = LocaleList("ja")
                            ),
                            modifier = Modifier.padding(10.dp),
                            label = { Text(text = "Kanji value") },
                            value = viewModel.kanjiValue,
                            onValueChange = viewModel::updateKanjiValue
                        )

                        TextField(
                            keyboardOptions = KeyboardOptions.Default.copy(
                                hintLocales = LocaleList("ja")
                            ),
                            modifier = Modifier.padding(10.dp),
                            label = { Text(text = "Kana value") },
                            value = viewModel.kanaValue,
                            onValueChange = viewModel::updateKanaValue
                        )

                        TextField(
                            keyboardOptions = KeyboardOptions.Default.copy(
                                hintLocales = LocaleList("en")
                            ),
                            modifier = Modifier.padding(10.dp),
                            label = { Text(text = "English value") },
                            value = viewModel.englishValue,
                            onValueChange = viewModel::updateEnglishValue
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = viewModel.difficulty == Difficulty.EASY,
                                onClick = { viewModel.updateDifficulty(Difficulty.EASY) })
                            Text("Easy")
                            RadioButton(
                                selected = viewModel.difficulty == Difficulty.MEDIUM,
                                onClick = { viewModel.updateDifficulty(Difficulty.MEDIUM) })
                            Text("Medium")
                            RadioButton(
                                selected = viewModel.difficulty == Difficulty.HARD,
                                onClick = { viewModel.updateDifficulty(Difficulty.HARD) })
                            Text("Hard")
                        }

                        Row {
                            Button(
                                content = { Text(text = "Add") },
                                enabled = viewModel.canSave(),
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        viewModel.saveCard()
                                        withContext(Dispatchers.Main) {
                                            setResult(RESULT_OK)
                                            finish()
                                        }
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
}
