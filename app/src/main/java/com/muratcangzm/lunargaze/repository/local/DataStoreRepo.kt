package com.muratcangzm.lunargaze.repository.local

import androidx.datastore.preferences.core.Preferences

interface DataStoreRepo {
    suspend fun saveDataStore(key: String, value: String)
    suspend fun readDataStore(key: String): String?
    suspend fun getAllKeys(): Set<Preferences.Key<*>>?
    suspend fun getAllValues(): Collection<String>?
    suspend fun getValueByKey(key: Preferences.Key<String>): String?
    suspend fun deleteNameFromPreferences(key: String)
    suspend fun deleteAllPreferences()
}
