package com.example.ransomsensei.activity_onboarding.components

import com.example.ransomsensei.R

import android.content.res.Resources
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ransomsensei.activity_onboarding.util.HomeAppInfo
import com.example.ransomsensei.activity_onboarding.viewmodels.SetHomeActivityViewModel
import com.example.ransomsensei.theme.AppTheme
import com.google.accompanist.drawablepainter.DrawablePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetHomeActivityScreen(
    viewModel: SetHomeActivityViewModel,
    navHostController: NavHostController
) {
    SetHomeActivityScreen(viewModel.activities,
        viewModel.selectedPackageName,
        onSelectPackageName = {it -> viewModel.selectedPackageName = it},
        onSave = viewModel::saveHomePackage,
        navigateToNextScreen = {
            navHostController.navigate(viewModel.nextDestination)
        })
}

@Composable
fun SetHomeActivityScreen(
    homeAppInfos: List<HomeAppInfo>,
    selectedPackageName: String,
    onSelectPackageName: (String) -> Unit,
    onSave: suspend () -> Unit,
    navigateToNextScreen: () -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Welcome to Ransom Sensei. Please select your default launcher.",
                    modifier = Modifier.width(240.dp)
                )
            }

            for (homeAppInfo in homeAppInfos) {
                Card(shape = MaterialTheme.shapes.medium) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.9f)
                            .padding(12.dp)
                    ) {
                        RadioButton(
                            onClick = {
                                onSelectPackageName(homeAppInfo.packageName)
                            },
                            selected = selectedPackageName == homeAppInfo.packageName
                        )
                        Image(
                            modifier = Modifier.padding((5.dp)),
                            painter = DrawablePainter(homeAppInfo.icon),
                            contentDescription = null
                        )
                        Text(homeAppInfo.label)
                    }
                }
            }

            Button(
                enabled = selectedPackageName.isNotEmpty(),
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        onSave()
                        withContext(Dispatchers.Main) {
                            navigateToNextScreen()
                        }
                    }
                }

            ) { Text("Select Launcher") }

            Button(
                enabled = true,
                onClick = navigateToNextScreen
            ) { Text("Skip for now") }
        }


    }
}

@Composable
@PreviewLightDark
fun SetHomeActivityScreenPreview() {
    AppTheme {
        val resources = Resources.getSystem()
        SetHomeActivityScreen(
            homeAppInfos = listOf(
                HomeAppInfo(
                    packageName = "home.activity.1",
                    label = "Home Activity 1",
                    icon = resources.getDrawable(
                        R.drawable.ic_launcher_foreground,
                        resources.newTheme()
                    )
                ),
                HomeAppInfo(
                    packageName = "home.activity.2",
                    label = "Home Activity 2",
                    icon = resources.getDrawable(
                        R.drawable.ic_launcher_foreground,
                        resources.newTheme()
                    )
                )
            ),
            selectedPackageName = "",
            onSelectPackageName = {},
            onSave = {},
            navigateToNextScreen = {}
        )
    }
}
