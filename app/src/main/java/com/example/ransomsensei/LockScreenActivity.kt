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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
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
                var response = remember { mutableStateOf("") }
                var homeActivityPackage = remember { mutableStateOf("") }
                var database: RansomSenseiDatabase = Room.databaseBuilder(
                    context = context,
                    RansomSenseiDatabase::class.java,
                    "ransomSensei"
                ).build()

                var kanaValue = remember { mutableStateOf("") }
                var kanjiValue = remember { mutableStateOf("") }
                var englishValue = remember { mutableStateOf("") }


                LaunchedEffect(key1 = Unit) {
                    homeActivityPackage.value = dataStoreManager.getHomeActivity()
                    val cards = database.cardDao().getAll()
                    val card = cards[Random.nextInt(cards.size)]
                    kanaValue.value = card.kanaValue ?: ""
                    kanjiValue.value = card.kanjiValue ?: ""
                    englishValue.value = card.englishValue ?: ""
                }

                Column(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row {
                        Text(kanjiValue.value)
                    }
                    Row {
                        Text(kanaValue.value)
                    }

                    TextField(
                        value = response.value,
                        onValueChange = { response.value = it })

                    Button(onClick = {
                        if (response.value == englishValue.value) {
                            val intent = Intent(Intent.ACTION_MAIN)
                            intent.addCategory(Intent.CATEGORY_HOME)
                            if (homeActivityPackage.value.isNotEmpty()) {
                                intent.setPackage(homeActivityPackage.value)
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
            }
        }
    }
}