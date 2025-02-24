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