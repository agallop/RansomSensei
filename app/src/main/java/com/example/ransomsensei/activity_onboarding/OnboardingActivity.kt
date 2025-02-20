package com.example.ransomsensei.activity_onboarding

import android.app.role.RoleManager
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
import androidx.compose.material3.Card
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ransomsensei.activity_onboarding.components.SetHomeActivityScreen
import com.example.ransomsensei.activity_onboarding.util.Destination
import com.example.ransomsensei.activity_onboarding.viewmodels.SetHomeActivityViewModel
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.theme.AppTheme
import com.google.accompanist.drawablepainter.DrawablePainter

import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            AppTheme {
                val navHostController = rememberNavController()
                NavHost(
                    navController = navHostController,
                    startDestination = Destination.SetHomeActivityScreen
                ) {
                    composable<Destination.SetHomeActivityScreen> {
                        val viewModel = koinViewModel<SetHomeActivityViewModel>()
                        viewModel.nextDestination = Destination.Finish
                        SetHomeActivityScreen(
                            navHostController = navHostController,
                            viewModel = viewModel
                        )
                    }
                    composable<Destination.Finish> {
                        finish()
                    }
                }
            }
        }
    }
}