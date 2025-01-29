package com.example.ransomsensei.ui

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.Difficulty
import com.example.ransomsensei.theme.AppTheme
import kotlinx.coroutines.launch

class AddTermActivity : ComponentActivity() {

    companion object {
        const val CARD_SET_ID_EXTRA = "CARD_SET_ID_EXTRA"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cardSetId = intent.getIntExtra(CARD_SET_ID_EXTRA, 0)


        setContent{
            AppTheme {
                Scaffold (
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
                                IconButton(onClick = {setResult(RESULT_CANCELED); finish()}) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
                                }
                            })
                    }
                ) { padding ->
                    val kanjiValue = remember { mutableStateOf("") }
                    val kanaValue = remember { mutableStateOf("") }
                    val englishValue = remember { mutableStateOf("") }
                    val difficulty = remember { mutableStateOf(Difficulty.UNKNOWN) }
                    val scope = rememberCoroutineScope()
                    val context = LocalContext.current

                    Column(
                            modifier = Modifier.padding(padding).fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                    ) {
                        TextField(
                            keyboardOptions = KeyboardOptions.Default.copy(
                                hintLocales = LocaleList("ja")
                            ),
                                modifier = Modifier.padding(10.dp),
                                label = { Text(text = "Kanji value") },
                                value = kanjiValue.value,
                                onValueChange = { kanjiValue.value = it })

                        TextField(
                            keyboardOptions = KeyboardOptions.Default.copy(
                                hintLocales = LocaleList("ja")
                            ),
                                modifier = Modifier.padding(10.dp),
                                label = { Text(text = "Kana value") },
                                value = kanaValue.value,
                                onValueChange = { kanaValue.value = it })

                        TextField(
                            keyboardOptions = KeyboardOptions.Default.copy(
                                hintLocales = LocaleList("en")
                            ),
                                modifier = Modifier.padding(10.dp),
                                label = { Text(text = "English value") },
                                value = englishValue.value,
                                onValueChange = { englishValue.value = it })

                        Row (verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                    selected = difficulty.value == Difficulty.EASY,
                                    onClick = { difficulty.value = Difficulty.EASY })
                            Text("Easy")
                            RadioButton(
                                    selected = difficulty.value == Difficulty.MEDIUM,
                                    onClick = { difficulty.value = Difficulty.MEDIUM })
                            Text("Medium")
                            RadioButton(
                                    selected = difficulty.value == Difficulty.HARD,
                                    onClick = { difficulty.value = Difficulty.HARD })
                            Text("Hard")
                        }

                        Row {
                            Button(
                                    content = {Text(text = "Add")},
                                    enabled = kanjiValue.value.isNotEmpty()
                                            && kanaValue.value.isNotEmpty()
                                            && englishValue.value.isNotEmpty()
                                            && difficulty.value != Difficulty.UNKNOWN,
                                    onClick = {
                                            scope.launch {
                                            RansomSenseiDatabase
                                                .getInstance(context)
                                                .cardDao()
                                                .insertCards(Card(
                                                    cardId = 0,
                                                    cardSetId = cardSetId,
                                                    kanaValue = kanaValue.value,
                                                    kanjiValue = kanjiValue.value,
                                                    englishValue = englishValue.value,
                                                    difficulty = difficulty.value
                                            ))
                                                setResult(RESULT_OK)
                                            finish()
                                    }
                                    })
                        }
                    }
                }
            }
        }
    }
}