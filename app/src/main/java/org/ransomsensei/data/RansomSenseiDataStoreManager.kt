package org.ransomsensei.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

const val SETTINGS_DATABASE = "settings"

val Context.preferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATABASE)

class RansomSenseiDataStoreManager(context: Context) {
    private val _dataStore = context.preferenceDataStore

    companion object {
        private val _HOME_ACTIVITY = stringPreferencesKey("HOME")
        private val _LAST_INTERACTION = stringPreferencesKey("LAST_INTERACTION")
        private val _FREQUENCY_CAP_MILLIS = stringPreferencesKey("FREQUENCY_CAP_MILLIS")
    }

    suspend fun saveHomePackage(home: String) {
        _dataStore.edit { settings ->
            settings[_HOME_ACTIVITY] = home
        }
    }

    fun getHomePackage(): Flow<String> {
        return _dataStore.data.map { preferences ->
            preferences[_HOME_ACTIVITY] ?: ""
        }
    }

    suspend fun setLastInteraction(timestamp: Long) {
        _dataStore.edit { settings ->
            settings[_LAST_INTERACTION] = timestamp.toString()
        }
    }

    suspend fun getLastInteraction(): Long {
        return _dataStore.data.map { preferences ->
            (preferences[_LAST_INTERACTION] ?: "0").toLong()
        }.first()
    }

    suspend fun setFrequencyCapMillis(millis: Long) {
        _dataStore.edit { settings ->
            settings[_FREQUENCY_CAP_MILLIS] = millis.toString()
        }
    }

    fun getFrequencyCapMillis(): Flow<Long> {
        return _dataStore.data.map { preferences ->
            (preferences[_FREQUENCY_CAP_MILLIS] ?: "0").toLong()
        }
    }

    suspend fun clearData() {
        _dataStore.edit {
            it.clear()
        }
    }
}
