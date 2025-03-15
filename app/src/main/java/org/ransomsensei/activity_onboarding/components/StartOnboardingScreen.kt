/*
 *  Copyright (c) 2025 Anthony Gallop <agallopdev@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ransomsensei.activity_onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.ransomsensei.activity_onboarding.viewmodels.StartOnboardingViewModel
import org.ransomsensei.theme.AppTheme

@Composable
fun StartOnBoardingScreen(
    viewModel: StartOnboardingViewModel,
    navHostController: NavHostController
) {
    StartOnBoardingScreen(
        navigateToNextScreen = {
            navHostController.navigate(viewModel.nextDestination)
        }
    )
}

@Composable
fun StartOnBoardingScreen(navigateToNextScreen: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Welcome to Ransom Sensei")
            Text("Let's get your app set up")
            Spacer(Modifier.height(40.dp))
            Button(onClick = navigateToNextScreen) {
                Text("Continue")
            }
        }

    }
}

@PreviewLightDark()
@Composable
fun StartOnBoardingScreenPreview() {
    AppTheme {
        StartOnBoardingScreen(
            navigateToNextScreen = {}
        )
    }
}