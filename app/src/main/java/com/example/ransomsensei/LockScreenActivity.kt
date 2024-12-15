package com.example.ransomsensei

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ransomsensei.data.RansomSenseiDataStoreManager

class LockScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val dataStoreContext = LocalContext.current
            val dataStoreManager = RansomSenseiDataStoreManager(context = dataStoreContext)
            var response = remember { mutableStateOf("") }
            var homeActivityPackage = remember { mutableStateOf("") }

            LaunchedEffect(key1 = Unit) {
                homeActivityPackage.value = dataStoreManager.getHomeActivity()
            }

            Column ( modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text("おはよう")

                TextField(
                    value = response.value,
                    onValueChange = { response.value = it })

                Button(onClick = {
                    if (response.value == "Good morning") {
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.addCategory(Intent.CATEGORY_HOME)
                        if (homeActivityPackage.value.isNotEmpty()) {
                            intent.setPackage(homeActivityPackage.value)
                        }
                        startActivity(intent)
                    }
                }) { Text("Check my answer") }
            }
        }
    }
}