package com.example.ransomsensei.ui

import android.app.role.RoleManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class SetDefaultAppActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roleManager: RoleManager = getSystemService(ROLE_SERVICE) as RoleManager
        val defaultAppIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)

        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("To use Ransom Sensei, you need to set it as your default home screen")

                Button(onClick = {
                    startActivityForResult(defaultAppIntent, 1)
                }) {
                    Text("Set as default home screen")
                }

                Button(onClick = { finish() }) {
                    Text("Skip for now")
                }

            }

        }
    }
}