package org.ransomsensei.activity_onboarding.components

import android.app.role.RoleManager
import android.content.Context
import android.content.Context.ROLE_SERVICE
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.ransomsensei.activity_onboarding.viewmodels.SetDefaultHomeAppViewModel
import org.ransomsensei.theme.AppTheme

@Composable
fun SetDefaultHomeAppScreen(
    context: Context,
    viewModel: SetDefaultHomeAppViewModel,
    navHostController: NavHostController
) {
    SetDefaultHomeAppScreen(
        setDefaultHome = { setDefault(context) },
        navigateToNextScreen = { navHostController.navigate(viewModel.nextDestination) }
    )

}

@Composable
fun SetDefaultHomeAppScreen(
    setDefaultHome: () -> Unit,
    navigateToNextScreen: () -> Unit
) {
    Scaffold { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Ransom Sensei needs to be set as your default home app to start " +
                            "quizzing you",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(600.dp)
                        .border(
                            width = 5.dp, shape = MaterialTheme.shapes.large,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Home",
                        Modifier
                            .padding(vertical = 16.dp)
                            .size(128.dp)
                    )
                }

                Button(onClick = {
                    setDefaultHome()
                }) {
                    Text(text = "Set as default home screen")
                }

                Button(onClick = navigateToNextScreen) {
                    Text("Skip for now")
                }
            }
        }
    }
}

fun setDefault(context: Context) {
    val defaultAppIntent =
        (context.getSystemService(ROLE_SERVICE) as RoleManager)
            .createRequestRoleIntent(RoleManager.ROLE_HOME)
    defaultAppIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    (context as ComponentActivity).startActivityForResult(defaultAppIntent, 1)
    context.finish()
}

@PreviewLightDark
@Composable
fun SetDefaultHomeAppScreenPreview() {
    AppTheme {
        SetDefaultHomeAppScreen(
            setDefaultHome = {},
            navigateToNextScreen = {}
        )
    }
}

