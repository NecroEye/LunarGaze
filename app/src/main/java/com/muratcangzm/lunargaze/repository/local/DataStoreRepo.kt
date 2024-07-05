package com.muratcangzm.lunargaze.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepo
@Inject
constructor
    (
    private val dataStore:
    DataStore<androidx.datastore.preferences.core.Preferences>
) {

    suspend fun saveDataStore(key: String, value: String) {

        val transformedKey = stringPreferencesKey(key)

        dataStore.edit { preference ->

            preference[transformedKey] = value

        }
    }

    suspend fun readDataStore(key: String): String? {

        val transformedKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()

        return preference[transformedKey]
    }

    suspend fun getAllKeys(): Set<Preferences.Key<*>>? {

        val keys = dataStore.data
            .map {
                it.asMap().keys
            }

        return keys.firstOrNull()

    }

    suspend fun getAllValues(): Collection<Any>? {

        val values = dataStore.data
            .map {
                it.asMap().values
            }

        return values.firstOrNull()
    }

    suspend fun getValueByKey(key: Preferences.Key<String>): String? {

        val value = dataStore.data
            .map {
                it[key]
            }

        return value.firstOrNull()
    }

    suspend fun deleteNameFromPreferences(key: String) {
        dataStore.edit { preference ->

            val transformedKey = stringPreferencesKey(key)

            preference.remove(transformedKey)
        }
    }

    suspend fun deleteAllPreferences() {
        dataStore.edit { preference ->
            preference.clear()
        }
    }


}