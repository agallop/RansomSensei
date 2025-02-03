package com.example.ransomsensei.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ransomsensei.data.RansomSenseiDataStoreManager

class SettingsViewModel(val dataStoreManager: RansomSenseiDataStoreManager) : ViewModel() {
    val homeActivity by mutableStateOf("")
}