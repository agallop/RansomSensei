package com.example.ransomsensei.ui.cardset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus
import com.example.ransomsensei.theme.AppTheme
import kotlinx.coroutines.launch

class EditCardSetActivity : ComponentActivity() {

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
                                    Text("Edit set")
                                },
                                navigationIcon = {
                                    IconButton(onClick = {setResult(RESULT_CANCELED); finish()}) {
                                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
                                    }
                                })
                        }
                        ) { padding ->
                    val name = remember { mutableStateOf("") }
                    val status = remember { mutableStateOf(CardSetStatus.ENABLED) }
                    val scope = rememberCoroutineScope()
                    val context = LocalContext.current

                    LaunchedEffect(key1 = Unit) {
                        val cardSet = RansomSenseiDatabase.getInstance(context).cardSetDao()
                            .getCardSet(cardSetId)
                        name.value = cardSet.cardSetName
                        status.value = cardSet.cardSetStatus
                    }

                    Column(
                        modifier = Modifier.padding(padding).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        TextField(
                            modifier = Modifier.padding(10.dp),
                            label = { Text(text = "Set name") },
                            value = name.value,
                            onValueChange = { name.value = it })

                        Row (verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = status.value == CardSetStatus.ENABLED,
                                onClick = { status.value = CardSetStatus.ENABLED })
                            Text("Active")
                            RadioButton(
                                selected = status.value == CardSetStatus.DISABLED,
                                onClick = { status.value = CardSetStatus.DISABLED })
                            Text("Inactive")
                        }

                        Row {
                            Button(
                                content = { Text(text = "Done") },
                                enabled = name.value.isNotEmpty()
                                        && status.value != CardSetStatus.UNKNOWN,
                                onClick = {
                                    scope.launch {
                                        RansomSenseiDatabase
                                            .getInstance(context)
                                            .cardSetDao()
                                            .updateCardSet(
                                                CardSet(
                                                    cardSetId = cardSetId,
                                                    cardSetName = name.value,
                                                    cardSetStatus = status.value
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
