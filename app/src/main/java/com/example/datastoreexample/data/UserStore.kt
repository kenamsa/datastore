package com.example.datastoreexample.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userToken")
        private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
        private val PSWD_TOKEN_KEY = stringPreferencesKey("pswd_token")
        private val SAVE_DATA_KEY = booleanPreferencesKey("save_data")
    }

    val getAccessToken: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_TOKEN_KEY] ?: ""
    }

    val getPasswordToken: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PSWD_TOKEN_KEY] ?: ""
    }

    val isDataSaved: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SAVE_DATA_KEY] ?: false
    }

    suspend fun saveToken(userToken: String, passwordToken: String, saveData: Boolean) {
        context.dataStore.edit { preferences ->
            if (saveData) {
                // Save user and password tokens and the saveData state
                preferences[USER_TOKEN_KEY] = userToken
                preferences[PSWD_TOKEN_KEY] = passwordToken
                preferences[SAVE_DATA_KEY] = saveData
            } else {
                // Remove user and password tokens if not saving data
                preferences.remove(USER_TOKEN_KEY)
                preferences.remove(PSWD_TOKEN_KEY)
            }
        }
    }

}

