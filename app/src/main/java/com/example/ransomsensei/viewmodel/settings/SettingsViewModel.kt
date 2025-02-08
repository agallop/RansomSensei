package com.example.ransomsensei.viewmodel.settings


import androidx.lifecycle.ViewModel
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import kotlinx.coroutines.flow.Flow

class SettingsViewModel(val dataStore: RansomSenseiDataStoreManager) : ViewModel(){
    var frequencyCap: Flow<Long> = dataStore.getFrequencyCapMillis()
}