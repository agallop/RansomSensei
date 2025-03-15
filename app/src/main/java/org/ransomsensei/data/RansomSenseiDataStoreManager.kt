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
package org.ransomsensei.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
        private val _IS_IN_BOARDING = booleanPreferencesKey("ON_BOARDING_FINISHED")
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

    suspend fun setIsInOnboarding(finished: Boolean) {
        _dataStore.edit { settings ->
            settings[_IS_IN_BOARDING] = finished
        }
    }

    fun getIsInOnboarding(): Flow<Boolean> {
        return _dataStore.data.map { preferences ->
            preferences[_IS_IN_BOARDING] == true
        }
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
