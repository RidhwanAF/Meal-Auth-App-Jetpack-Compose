package com.raf.mealrecipe.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raf.mealrecipe.data.local.dao.MealRecipeDao
import com.raf.mealrecipe.data.local.entity.AreaCategoriesEntity
import com.raf.mealrecipe.data.local.entity.BookmarkEntity
import com.raf.mealrecipe.data.local.entity.FoodCategoriesEntity

@Database(
    entities = [AreaCategoriesEntity::class, FoodCategoriesEntity::class, BookmarkEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MealRecipeDatabase : RoomDatabase() {

    abstract fun mealRecipeDao(): MealRecipeDao

    companion object {
        private const val DB_NAME = "meal_recipe.db"

        @Volatile
        private var INSTANCE: MealRecipeDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): MealRecipeDatabase {
            if (INSTANCE == null) {
                synchronized(MealRecipeDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MealRecipeDatabase::class.java, DB_NAME
                    )
                        .build()
                }
            }
            return INSTANCE as MealRecipeDatabase
        }
    }
}