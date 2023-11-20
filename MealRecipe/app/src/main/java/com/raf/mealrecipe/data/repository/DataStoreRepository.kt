package com.raf.mealrecipe.data.repository

import com.raf.mealrecipe.utility.DataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepository @Inject constructor(private val dataStore: DataStore) {

    suspend fun setDynamicColorState(value: Boolean) {
        dataStore.setDynamicColorState(value)
    }

    fun getDynamicColorState(): Flow<Boolean> {
        return dataStore.getDynamicColorState()
    }

    suspend fun setDarkThemeState(value: Boolean) {
        dataStore.setDarkThemeState(value)
    }

    fun getDarkThemeState(): Flow<Boolean> {
        return dataStore.getDarkThemeState()
    }
}