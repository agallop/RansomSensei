package com.example.ransomsensei

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class LockScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val dataStoreContext = LocalContext.current
            val dataStoreManager = RansomSenseiDataStoreManager(context = dataStoreContext)
            val scope = rememberCoroutineScope()
            var response = remember { mutableStateOf("") }
            var homeActivityPackage = remember { mutableStateOf("") }

            LaunchedEffect(true) {
                scope.launch {
                    homeActivityPackage.value =
                        dataStoreManager.getHomeActivity().single()
                    println("==========")
                    println("==========")
                    println("==========")
                    println(homeActivityPackage.value)
                    println("==========")
                    println("==========")
                    println("==========")
                }
            }

            Column ( modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text("おはよう")

                TextField(
                    value = response.value,
                    onValueChange = { response.value = it })

                Button(onClick = {
                    if (response.value.equals("Good morning")) {
                        val intent = Intent(Intent.ACTION_MAIN)
                        // intent.addCategory(Intent.CATEGORY_HOME)
                        println(homeActivityPackage.value)
                        intent.setPackage(homeActivityPackage.value)
                        startActivity(intent)
                    }
                }) { Text("Check my answer") }
            }

        }
    }

  /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_screen)

        val unlockButton: Button = findViewById(R.id.unlock_button)
        val response : TextInputEditText = findViewById(R.id.response)

        unlockButton.setOnClickListener {
            if (response.text.contentEquals("Good morning")) {

                val launcher = Intent(Intent.ACTION_MAIN)
                launcher.addCategory(Intent.CATEGORY_HOME)

                val chooser = Intent.createChooser(launcher, null)
                startActivity(chooser)
                finish()
            }
        }
    } */
}