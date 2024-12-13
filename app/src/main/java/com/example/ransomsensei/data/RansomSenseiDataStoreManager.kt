package com.example.ransomsensei.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val SETTINGS_DATABASE = "settings"

val Context.preferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATABASE)

class RansomSenseiDataStoreManager(val context: Context){

    val HOME_ACTIVITY = stringPreferencesKey("HOME")

    suspend fun saveHomeActivity(home: String) {
        context.preferenceDataStore.edit {
            settings ->
            settings[HOME_ACTIVITY] = home
        }
    }

     fun getHomeActivity() :Flow<String> {
        return context.preferenceDataStore.data.map {
            preferences ->
            preferences[HOME_ACTIVITY] ?: ""
        }

    }
}
