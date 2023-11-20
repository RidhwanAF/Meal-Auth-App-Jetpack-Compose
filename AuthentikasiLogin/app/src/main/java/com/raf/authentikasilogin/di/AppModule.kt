package com.raf.authentikasilogin.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.raf.authentikasilogin.local.UserDatabase
import com.raf.authentikasilogin.network.api.ApiConfig
import com.raf.authentikasilogin.network.api.ApiService
import com.raf.authentikasilogin.network.response.DataItem
import com.raf.authentikasilogin.remote.UserRemoteMediator
import com.raf.authentikasilogin.repository.DataStoreRepository
import com.raf.authentikasilogin.repository.LocalRepository
import com.raf.authentikasilogin.utility.DataStore
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
        return ApiConfig.getLoginApi()
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
    fun provideLocalRepository(userDb: UserDatabase): LocalRepository {
        return LocalRepository(userDb)
    }

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return UserDatabase.getDatabase(context)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideUserPager(
        apiService: ApiService,
        userDb: UserDatabase,
        localRepository: LocalRepository
    ): Pager<Int, DataItem> {
        return Pager(
            config = PagingConfig(pageSize = 6),
            remoteMediator = UserRemoteMediator(
                apiService = apiService,
                database = userDb,
                localRepository = localRepository
            ),
            pagingSourceFactory = {
                localRepository.getAllUsers()
            }
        )
    }

}