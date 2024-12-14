package com.example.ransomsensei
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val dataStoreContext = LocalContext.current
            val dataStoreManager = RansomSenseiDataStoreManager(context = dataStoreContext)
            val scope = rememberCoroutineScope()
            val WelcomeActivityIntent = Intent(this, WelcomeActivity::class.java)
            var needToSetHomeActivity = remember { mutableStateOf(false) }

            LaunchedEffect(true) {
                scope.launch {
                    needToSetHomeActivity.value =
                        dataStoreManager.getHomeActivity().single().isNotEmpty()
                }
            }

            if (needToSetHomeActivity.value) {
                startActivity(WelcomeActivityIntent)
            }

            Text("TBD")
        }
    }
}