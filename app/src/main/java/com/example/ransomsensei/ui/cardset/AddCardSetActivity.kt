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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.data.entity.CardSetStatus
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.viewmodel.cardset.AddCardSetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

class AddCardSetActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    setContent{
        AppTheme {
        val viewModel = koinViewModel<AddCardSetViewModel>()
            Scaffold (
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text("Add new set")
                        },
                        navigationIcon = {
                            IconButton(onClick = {setResult(RESULT_CANCELED); finish()}) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back button")
                            }
                        })
                }
            ) { padding ->

                Column(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    TextField(
                        modifier = Modifier.padding(10.dp),
                        label = { Text(text = "Set name") },
                        value = viewModel.cardSetName,
                        onValueChange = { viewModel.cardSetName = it })

                    Row (verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = viewModel.status == CardSetStatus.ENABLED,
                            onClick = { viewModel.status = CardSetStatus.ENABLED })
                        Text("Enabled")
                        RadioButton(
                            selected = viewModel.status == CardSetStatus.DISABLED,
                            onClick = { viewModel.status = CardSetStatus.DISABLED })
                        Text("Disabled")
                    }

                    Row {
                        Button(
                            content = { Text(text = "Add") },
                            enabled = viewModel.cardSetName.isNotEmpty()
                                    && viewModel.status != CardSetStatus.UNKNOWN,
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    viewModel.addCardSet()
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
