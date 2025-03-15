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
package org.ransomsensei.activity_onboarding.viewmodels

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.ransomsensei.activity_onboarding.util.Destination
import org.ransomsensei.activity_onboarding.util.HomeAppInfo
import org.ransomsensei.data.RansomSenseiDataRepository

class SetHomeActivityViewModel(
    packageManager: PackageManager,
    private val _repository: RansomSenseiDataRepository
) : ViewModel() {
    var activities = mutableStateListOf<HomeAppInfo>()
    var selectedPackageName by mutableStateOf("")
    var nextDestination: Destination = Destination.Finish


    init {
        val launcher = Intent(Intent.ACTION_MAIN)
        launcher.addCategory(Intent.CATEGORY_HOME)
        val resolveInfos: List<ResolveInfo> =
            packageManager.queryIntentActivities(launcher, PackageManager.MATCH_ALL)

        if (resolveInfos.isNotEmpty()) {
            for (packageInfo in resolveInfos) {
                val label = packageInfo.loadLabel(packageManager).toString()
                if (label.isNotEmpty() &&
                    !packageInfo.activityInfo.packageName.contains("ransomsensei")
                ) {
                    val icon = packageInfo.loadIcon(packageManager)
                    activities.add(
                        HomeAppInfo(
                            packageName = packageInfo.activityInfo.packageName,
                            label = label,
                            icon = icon
                        )
                    )
                }
            }
        }
    }

    suspend fun saveHomePackage() {
        _repository.saveHomePackage(selectedPackageName)
    }
}