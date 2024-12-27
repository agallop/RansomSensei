package com.example.ransomsensei

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.ui.SetDefaultAppActivity
import com.google.accompanist.drawablepainter.DrawablePainter

import kotlinx.coroutines.launch

class WelcomeActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val launcher = Intent(Intent.ACTION_MAIN)
        launcher.addCategory(Intent.CATEGORY_HOME)
        val packageManager = packageManager
        val resolveInfos: List<ResolveInfo> =
            packageManager.queryIntentActivities(launcher, PackageManager.MATCH_ALL)

        if (resolveInfos.isNotEmpty()) {
            for (resolveInfo in resolveInfos) {
                println(resolveInfo.activityInfo.packageName)
                println(resolveInfo.iconResource)
            }
        }

        setContent {
            val context = LocalContext.current
            val dataStoreManager = RansomSenseiDataStoreManager(context = context)

            ChooseHomeActivityScreen(
                modifier = Modifier.fillMaxSize(),
                packageInfos = resolveInfos, dataStoreManager)
        }
    }

    @Composable
    fun ChooseHomeActivityScreen(modifier: Modifier = Modifier,
                                 packageInfos: List<ResolveInfo>,
                                 dataStore: RansomSenseiDataStoreManager) {

        AppTheme {
            val scope = rememberCoroutineScope()
            var selected = remember { mutableStateOf("") }
            val setDefaultAppActivityIntent = Intent(this, SetDefaultAppActivity::class.java)
            setDefaultAppActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


            Scaffold { padding ->
                Column(
                    modifier = modifier.padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Welcome to Ransom Sensei. Please select your default launcher.",
                            modifier = Modifier.width(240.dp)
                        )
                    }

                    for (packageInfo in packageInfos) {
                        val label = packageInfo.loadLabel(packageManager).toString()
                        if (label.isNotEmpty() &&
                            !packageInfo.activityInfo.packageName.contains("ransomsensei")
                        ) {
                                androidx.compose.material3.Card(shape = MaterialTheme.shapes.medium){

                                    Row(
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth(fraction = 0.9f).padding(12.dp)
                                    ) {
                                    RadioButton(
                                        onClick = {
                                            selected.value = packageInfo.activityInfo.packageName
                                        },
                                        selected = selected.value == packageInfo.activityInfo.packageName
                                    )
                                    Image(
                                        modifier = Modifier.padding((5.dp)),
                                        painter = DrawablePainter(
                                            packageInfo.loadIcon(
                                                packageManager
                                            )
                                        ),
                                        contentDescription = null
                                    )
                                    Text(label)
                                }
                            }
                        }
                    }

                    Button(
                        enabled = selected.value.isNotEmpty(),
                        onClick = {
                            scope.launch {
                                dataStore.saveHomeActivity(selected.value)
                                startActivity(setDefaultAppActivityIntent)
                                finish()
                            }
                        }

                    ) { Text("Select Launcher") }

                    Button(
                        enabled = true,
                        onClick = {
                            startActivity(setDefaultAppActivityIntent)
                            finish()
                        }
                    ) { Text("Skip for now") }
                }
            }
        }
    }
}