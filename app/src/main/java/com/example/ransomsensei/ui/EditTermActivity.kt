package com.example.ransomsensei.ui

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.Difficulty
import com.example.ransomsensei.theme.AppTheme
import kotlinx.coroutines.launch

class EditTermActivity : ComponentActivity() {

    companion object {
        const val CARD_ID_EXTRA = "CARD_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cardId = intent.getIntExtra(CARD_ID_EXTRA, 0)

        setContent {
            AppTheme {
                Scaffold { padding ->
                    val kanjiValue = remember { mutableStateOf("") }
                    val kanaValue = remember { mutableStateOf("") }
                    val englishValue = remember { mutableStateOf("") }
                    val difficulty = remember { mutableStateOf(Difficulty.UNKNOWN) }
                    val cardSetId = remember { mutableIntStateOf(0) }
                    val scope = rememberCoroutineScope()
                    val context = LocalContext.current

                    LaunchedEffect(key1 = Unit) {
                        val card = RansomSenseiDatabase.getInstance(context).cardDao()
                            .getCard(cardId)
                        kanaValue.value = card.kanaValue
                        kanjiValue.value = card.kanjiValue
                        englishValue.value = card.englishValue
                        difficulty.value = card.difficulty
                        cardSetId.intValue = card.cardSetId
                    }

                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        TextField(
                            modifier = Modifier.padding(10.dp),
                            label = { Text(text = "Kanji value") },
                            value = kanjiValue.value,
                            onValueChange = { kanjiValue.value = it })

                        TextField(
                            modifier = Modifier.padding(10.dp),
                            label = { Text(text = "Kana value") },
                            value = kanaValue.value,
                            onValueChange = { kanaValue.value = it })

                        TextField(
                            modifier = Modifier.padding(10.dp),
                            label = { Text(text = "English value") },
                            value = englishValue.value,
                            onValueChange = { englishValue.value = it })

                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                content = { Text(text = "Save") },
                                enabled = kanjiValue.value.isNotEmpty()
                                        && kanaValue.value.isNotEmpty()
                                        && englishValue.value.isNotEmpty()
                                        && difficulty.value != Difficulty.UNKNOWN,
                                onClick = {
                                    scope.launch {
                                        RansomSenseiDatabase.getInstance(context).cardDao()
                                            .insertCards(
                                                Card(
                                                    cardId = cardId,
                                                    cardSetId = cardSetId.intValue,
                                                    kanaValue = kanaValue.value,
                                                    kanjiValue = kanjiValue.value,
                                                    englishValue = englishValue.value,
                                                    difficulty = difficulty.value
                                                )
                                            )
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