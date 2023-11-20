package com.raf.authentikasilogin.repository

import androidx.paging.PagingSource
import com.raf.authentikasilogin.local.UserDatabase
import com.raf.authentikasilogin.network.response.DataItem
import com.raf.authentikasilogin.network.response.ListUserResponse
import javax.inject.Inject

class LocalRepository @Inject constructor(db: UserDatabase) {
    private val dao = db.userDao()

    fun getAllUsers(): PagingSource<Int, DataItem> {
        return dao.getAllUsers()
    }

    suspend fun insertUser(users: DataItem) {
        dao.insertUser(users)
    }

    suspend fun deleteUsers() {
        dao.deleteUsers()
    }
}