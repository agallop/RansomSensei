package com.example.ransomsensei

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.drawablepainter.DrawablePainter

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
            IndeterminateCircularIndicator(
                modifier = Modifier.fillMaxSize(),
                packageInfos = resolveInfos)
        }
    }

    @Preview
    @Composable
    fun PreviewMessageCard() {
        IndeterminateCircularIndicator(packageInfos = listOf())
    }

    @Composable
    fun IndeterminateCircularIndicator(modifier: Modifier = Modifier,
                                       packageInfos: List<ResolveInfo>) {
        Column ( modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            for (packageInfo in packageInfos) {
                Row (horizontalArrangement = Arrangement.Start) {
                    Image(
                        painter = DrawablePainter(packageInfo.loadIcon(packageManager)),
                        contentDescription = null
                    )
                    Text(packageInfo.loadLabel(packageManager).toString())

                    Checkbox(checked = packageInfo.isDefault, enabled = false, onCheckedChange = {})
                }
            }
        }
    }
}