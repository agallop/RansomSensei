package com.example.ransomsensei

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.theme.AppTheme
import kotlin.random.Random

class LockScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Scaffold {  padding ->
                    val context = LocalContext.current
                    val dataStoreManager = RansomSenseiDataStoreManager(context = context)
                    var homeActivityPackage = remember { mutableStateOf("") }
                    var isLoading = remember { mutableStateOf(true) }
                    var card = remember { mutableStateOf<Card?>(null) }

                LaunchedEffect(key1 = Unit) {
                    homeActivityPackage.value = dataStoreManager.getHomeActivity()
                    val cards = RansomSenseiDatabase.getInstance(context).cardDao().getAll()
                    if (cards.isNotEmpty()) {
                        card.value = cards[Random.nextInt(cards.size)]
                    }
                    isLoading.value = false
                }

                Column(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if(!isLoading.value) {
                        if (card.value != null) {
                            BasicQuestion(card.value!!, homeActivityPackage.value)
                        } else {
                            Button(content = {Text("Proceed")}, onClick = {
                                if (homeActivityPackage.value.isNotEmpty()) {
                                    intent.setPackage(homeActivityPackage.value)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                } else {
                                    val chooser = Intent.createChooser(intent, /* title */ null)
                                    startActivity(chooser)
                                }
                            })
                            }
                        }
                    }
                }
            }
        }
    }









    @Composable
    fun BasicQuestion(card: Card, homeActivityPackage: String) {
        var response = remember { mutableStateOf("") }

        var card = remember { card }
            Row {
                Text(card.kanjiValue?: "")
            }
            Row {
                Text(card.kanaValue?: "")
            }

            TextField(
                value = response.value,
                onValueChange = { response.value = it })

            Button(onClick = {
                if (response.value == (card.englishValue ?: "")) {
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
                }
            }) { Text("Check my answer") }
        }
}