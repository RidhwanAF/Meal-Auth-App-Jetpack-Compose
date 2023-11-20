package com.raf.authentikasilogin.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raf.authentikasilogin.network.response.DataItem

@Dao
interface UserDao {
    @Query("SELECT * FROM user_entity")
    fun getAllUsers(): PagingSource<Int, DataItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(users: DataItem)

    @Query("DELETE FROM user_entity")
    suspend fun deleteUsers()
}