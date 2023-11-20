package com.raf.mealrecipe.utility

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStore @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_preference")
    private val onDynamicColorStateKey = booleanPreferencesKey("dynamic_color_state_key")
    private val darkThemeStateKey = booleanPreferencesKey("dark_theme_state_key")


    suspend fun setDynamicColorState(value: Boolean) {
        context.dataStore.edit { pref ->
            pref[onDynamicColorStateKey] = value
        }
    }

    fun getDynamicColorState(): Flow<Boolean> = context.dataStore.data.map { pref ->
        pref[onDynamicColorStateKey] ?: false
    }

    suspend fun setDarkThemeState(value: Boolean) {
        context.dataStore.edit { pref ->
            pref[darkThemeStateKey] = value
        }
    }

    fun getDarkThemeState(): Flow<Boolean> = context.dataStore.data.map { pref ->
        pref[darkThemeStateKey] ?: false
    }
}