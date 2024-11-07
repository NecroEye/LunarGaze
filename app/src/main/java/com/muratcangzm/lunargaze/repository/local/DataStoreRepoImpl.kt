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
    private val encryptionHelper: EncryptionHelper // Inject the EncryptionHelper for encryption/decryption
) : DataStoreRepo {

    override suspend fun saveDataStore(key: String, value: String) {
        val transformedKey = stringPreferencesKey(key)

        // Encrypt the value using EncryptionHelper
        val encryptedValue = encryptionHelper.encrypt(value)

        // Save the encrypted value to DataStore
        dataStore.edit { preferences ->
            preferences[transformedKey] = encryptedValue
        }
    }

    override suspend fun readDataStore(key: String): String? {
        val transformedKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()

        // Retrieve and decrypt the value from DataStore
        return preferences[transformedKey]?.let { encryptedValue ->
            encryptionHelper.decrypt(encryptedValue)
        }
    }

    override suspend fun getAllKeys(): Set<Preferences.Key<*>>? {
        val keys = dataStore.data
            .map { it.asMap().keys }

        return keys.first()
    }

    override suspend fun getAllValues(): Collection<String>? {
        return try {
            dataStore.data
                .map { preferences ->
                    preferences.asMap().values.mapNotNull { value ->
                        // Decrypt each value in the preferences
                        try {
                            (value as? String?).let { encryptionHelper.decrypt(it!!) }
                        } catch (e: IllegalArgumentException) {
                            null
                        }
                    }
                }
                .firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getValueByKey(key: Preferences.Key<String>): String? {
        val preferences = dataStore.data.first()
        return preferences[key]?.let { encryptionHelper.decrypt(it) }
    }

    override suspend fun deleteNameFromPreferences(key: String) {
        val transformedKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences.remove(transformedKey)
        }
    }

    override suspend fun deleteAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
