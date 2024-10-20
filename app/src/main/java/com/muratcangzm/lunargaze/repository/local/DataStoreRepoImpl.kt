package com.muratcangzm.lunargaze.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.muratcangzm.lunargaze.helper.EncryptionHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepoImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val encryptionHelper: EncryptionHelper
) : DataStoreRepo {

    override suspend fun saveDataStore(key: String, value: String) {
        val transformedKey = stringPreferencesKey(key)
        val encryptedValue = encryptionHelper.encrypt(value)

        dataStore.edit { preference ->
            preference[transformedKey] = encryptedValue
        }
    }

    override suspend fun readDataStore(key: String): String? {
        val transformedKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()

        return preference[transformedKey]?.let { encryptionHelper.decrypt(it) }
    }

    override suspend fun getAllKeys(): Set<Preferences.Key<*>>? {
        val keys = dataStore.data
            .map { it.asMap().keys }

        return keys.first()
    }

    override suspend fun getAllValues(): Collection<String>? {
        val values = dataStore.data
            .map { preferences ->
                preferences.asMap().values.mapNotNull { encryptedValue ->
                    if (encryptedValue is String) {
                        try {
                            encryptionHelper.decrypt(encryptedValue)
                        } catch (e: IllegalArgumentException) {
                            null
                        }
                    } else {
                        null
                    }
                }
            }

        return values.firstOrNull()
    }

    override suspend fun getValueByKey(key: Preferences.Key<String>): String? {
        val value = dataStore.data
            .map { it[key] }

        return value.firstOrNull()
    }

    override suspend fun deleteNameFromPreferences(key: String) {
        val transformedKey = stringPreferencesKey(key)
        dataStore.edit { preference ->
            preference.remove(transformedKey)
        }
    }

    override suspend fun deleteAllPreferences() {
        dataStore.edit { preference ->
            preference.clear()
        }
    }
}
