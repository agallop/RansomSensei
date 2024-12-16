package com.example.ransomsensei.ui

import android.app.role.RoleManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.theme.AppTheme

class SetDefaultAppActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roleManager: RoleManager = getSystemService(ROLE_SERVICE) as RoleManager
        val defaultAppIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)

        setContent {
            AppTheme {
                Scaffold { padding ->

                    Column (modifier = Modifier.fillMaxSize().padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Text(text= "To use Ransom Sensei, you need to set it as your default home screen.",
                            modifier = Modifier.width(240.dp))

                        Button(onClick = {
                            startActivityForResult(defaultAppIntent, 1)
                        }) {
                            Text(text = "Set as default home screen")
                        }

                        Button(onClick = { finish() }) {
                            Text("Skip for now")
                        }

                }
            }
        }
    }
}
}