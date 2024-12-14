package com.example.ransomsensei

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
            val dataStoreContext = LocalContext.current
            val dataStoreManager = RansomSenseiDataStoreManager(context = dataStoreContext)

            IndeterminateCircularIndicator(
                modifier = Modifier.fillMaxSize(),
                packageInfos = resolveInfos, dataStoreManager, dataStoreContext)
        }
    }

    @Composable
    fun IndeterminateCircularIndicator(modifier: Modifier = Modifier,
                                       packageInfos: List<ResolveInfo>,
                                       dataStore: RansomSenseiDataStoreManager,
                                       context: Context) {
        val scope = rememberCoroutineScope()
        var selected = remember { mutableStateOf("") }

        Column ( modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Row (horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)) {
                Text(text = "Welcome to Ransom Sensei. Please select your default launcher.",
                    modifier = Modifier.width(240.dp))
            }

            for (packageInfo in packageInfos) {
                val label = packageInfo.loadLabel(packageManager).toString()
                if(label.isNotEmpty() &&
                    !packageInfo.activityInfo.packageName.contains("ransomsensei")) {
                    Row (horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)) {
                        Button(
                            enabled = true,
                            modifier = Modifier.widthIn(min = 240.dp),
                            onClick = {
                            selected.value = packageInfo.activityInfo.packageName
                        }) {
                            Image(
                                modifier = Modifier.padding((5.dp)),
                                painter = DrawablePainter(packageInfo.loadIcon(packageManager)),
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
                    if (selected.value.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Please select a home activity",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        scope.launch {
                            dataStore.saveHomeActivity(selected.value)
                            finish()
                        }
                    }
                }

            ) { Text("Select Launcher") }

            Button(
                enabled = true,
                onClick = {
                    finish()
                }

            ) { Text("Skip for now")}
        }
    }
}