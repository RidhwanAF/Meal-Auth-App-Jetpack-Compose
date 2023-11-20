package com.raf.authentikasilogin.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raf.authentikasilogin.local.dao.UserDao
import com.raf.authentikasilogin.network.response.DataItem

@Database(
    entities = [DataItem::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private const val DB_NAME = "user.db"

        @Volatile
        private var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserDatabase {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java, DB_NAME
                    )
                        .build()
                }
            }
            return INSTANCE as UserDatabase
        }
    }
}