package com.muratcangzm.lunargaze.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreRepo
@Inject
constructor
    (
    private val dataStore:
    DataStore<androidx.datastore.preferences.core.Preferences>
) {

    companion object {
        private val fileNameKey = stringPreferencesKey("FileKey")
    }

    suspend fun saveDataStore(fileName: String) {

        dataStore.edit { preference ->

            preference[fileNameKey] = fileName

        }
    }

    val getAllData: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[fileNameKey]
        }

    suspend fun deleteNameFromPreferences() {
        dataStore.edit { preference ->
            preference.remove(fileNameKey)
        }
    }

    suspend fun deleteAllPreferences() {
        dataStore.edit { preference ->
            preference.clear()
        }
    }


}