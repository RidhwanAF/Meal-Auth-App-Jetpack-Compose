package com.raf.authentikasilogin.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.raf.authentikasilogin.local.UserDatabase
import com.raf.authentikasilogin.network.api.ApiService
import com.raf.authentikasilogin.network.response.DataItem
import com.raf.authentikasilogin.repository.LocalRepository

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val apiService: ApiService,
    private val database: UserDatabase,
    private val localRepository: LocalRepository,
) : RemoteMediator<Int, DataItem>() {

    private var curentPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DataItem>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    if (state.pages.lastOrNull()?.data?.isEmpty() == true) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        val newPage = curentPage + 1
                        curentPage = newPage
                        curentPage
                    }
                }
            }

            val response = apiService.getUsers(loadKey ?: 1)
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localRepository.deleteUsers()
                }
                response.data.forEach { user ->
                    localRepository.insertUser(user)
                }
            }
            MediatorResult.Success(endOfPaginationReached = response.page == response.totalPages)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}