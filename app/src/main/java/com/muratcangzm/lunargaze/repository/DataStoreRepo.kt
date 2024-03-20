package com.muratcangzm.lunargaze.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.muratcangzm.lunargaze.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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

    suspend fun saveDataStore(key:String, value: String) {

        val fileKey = stringPreferencesKey(key)

        dataStore.edit { preference ->

            preference[fileKey] = value

        }
    }

    suspend fun readDataStore(key: String): String?{

        val fileKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()

        return preference[fileKey]
    }

   suspend fun getAllKeys(): Set<Preferences.Key<*>>? {

       val keys = dataStore.data
           .map{
               it.asMap().keys
           }

       return keys.firstOrNull()

   }

    suspend fun deleteNameFromPreferences(key: String) {
        dataStore.edit { preference ->

            val fileKey = stringPreferencesKey(key)

            preference.remove(fileKey)
        }
    }

    suspend fun getValueByKey(key:Preferences.Key<String>) : String?{

      val value = dataStore.data
          .map {
              it[key]
          }

            return value.firstOrNull()
    }

    suspend fun deleteAllPreferences() {
        dataStore.edit { preference ->
            preference.clear()
        }
    }


}