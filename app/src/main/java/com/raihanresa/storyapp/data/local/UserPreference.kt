package com.raihanresa.storyapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreference(private val context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val tokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreference(context)
            }.also { INSTANCE = it }
        }
    }
}