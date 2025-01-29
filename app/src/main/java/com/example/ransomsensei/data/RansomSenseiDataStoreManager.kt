package com.example.ransomsensei.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.protobuf.Timestamp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

const val SETTINGS_DATABASE = "settings"

val Context.preferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATABASE)

class RansomSenseiDataStoreManager(val context: Context){

    val HOME_ACTIVITY = stringPreferencesKey("HOME")
    val LAST_INTERACTION = stringPreferencesKey("LAST_INTERACTION")
    val FREQUENCY_CAP_MILLIS = stringPreferencesKey("FREQUENCY_CAP_MILLIS")

    suspend fun saveHomeActivity(home: String) {
        context.preferenceDataStore.edit {
            settings ->
            settings[HOME_ACTIVITY] = home
        }
    }

    suspend fun getHomeActivity() : String {
        return context.preferenceDataStore.data.map {
            preferences ->
            preferences[HOME_ACTIVITY] ?: ""
        }.first()
    }

    suspend fun setLastInteraction(timestamp: Long) {
        context.preferenceDataStore.edit {
                settings ->
            settings[LAST_INTERACTION] = timestamp.toString()
        }
    }

    suspend fun getLastInteraction() : Long {
        return context.preferenceDataStore.data.map {
                preferences ->
            (preferences[LAST_INTERACTION] ?: "0").toLong()
        }.first()
    }

    suspend fun setFrequencyCapMillis(millis: Long) {
        context.preferenceDataStore.edit {
                settings ->
            settings[FREQUENCY_CAP_MILLIS] = millis.toString()
        }
    }

    suspend fun getFrequencyCapMillis() : Long {
        return context.preferenceDataStore.data.map {
                preferences ->
            (preferences[FREQUENCY_CAP_MILLIS] ?: "0").toLong()
        }.first()
    }

    suspend fun clearData() {
        context.preferenceDataStore.edit {
            it.clear()
        }
    }
}
