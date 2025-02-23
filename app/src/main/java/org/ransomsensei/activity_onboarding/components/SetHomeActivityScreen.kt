package org.ransomsensei.activity_onboarding.components

import org.ransomsensei.R

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavHostController
import org.ransomsensei.activity_onboarding.util.HomeAppInfo
import org.ransomsensei.activity_onboarding.viewmodels.SetHomeActivityViewModel
import org.ransomsensei.theme.AppTheme
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
        onSelectPackageName = { it -> viewModel.selectedPackageName = it },
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
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "We need to determine which app is currently your home screen, " +
                                "so we can be sure to send you to the right place.",
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "You'll likely only see one option, but just in case, this is " +
                                "what we found on your device.",
                        modifier = Modifier.width(240.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            for (homeAppInfo in homeAppInfos) {
                Card(shape = MaterialTheme.shapes.medium) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.9f)
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
                    icon = ResourcesCompat.getDrawable(
                        resources,
                        R.mipmap.ic_launcher,
                        resources.newTheme()
                    )!!
                ),
                HomeAppInfo(
                    packageName = "home.activity.2",
                    label = "Home Activity 2",
                    icon = ResourcesCompat.getDrawable(
                        resources,
                        R.mipmap.ic_launcher,
                        resources.newTheme()
                    )!!
                )
            ),
            selectedPackageName = "",
            onSelectPackageName = {},
            onSave = {},
            navigateToNextScreen = {}
        )
    }
}
