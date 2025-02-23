package com.example.ransomsensei.activity_onboarding.viewmodels

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.ransomsensei.activity_onboarding.util.Destination
import com.example.ransomsensei.activity_onboarding.util.HomeAppInfo
import com.example.ransomsensei.data.RansomSenseiDataStoreManager

class SetHomeActivityViewModel(packageManager: PackageManager, val ransomSenseiDataStoreManager: RansomSenseiDataStoreManager) : ViewModel() {
    var activities = mutableStateListOf<HomeAppInfo>()
    var selectedPackageName by mutableStateOf("")
    var nextDestination : Destination = Destination.Finish


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
        ransomSenseiDataStoreManager.saveHomeActivity(selectedPackageName)
    }
}