package com.raf.mealrecipe.di

import android.content.Context
import com.raf.mealrecipe.data.local.db.MealRecipeDatabase
import com.raf.mealrecipe.data.network.api.ApiConfig
import com.raf.mealrecipe.data.network.api.ApiService
import com.raf.mealrecipe.data.repository.DataStoreRepository
import com.raf.mealrecipe.utility.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiConfig.getMealApiService()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore {
        return DataStore(context)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStore: DataStore): DataStoreRepository {
        return DataStoreRepository(dataStore)
    }

    @Provides
    @Singleton
    fun provideMealRecipeDatabase(@ApplicationContext context: Context): MealRecipeDatabase {
        return MealRecipeDatabase.getDatabase(context)
    }

}