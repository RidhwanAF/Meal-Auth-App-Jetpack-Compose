package com.raf.mealrecipe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raf.mealrecipe.data.local.entity.AreaCategoriesEntity
import com.raf.mealrecipe.data.local.entity.BookmarkEntity
import com.raf.mealrecipe.data.local.entity.FoodCategoriesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealRecipeDao {
    @Query("SELECT * FROM area_categories_entity")
    suspend fun getAllAreaCategories(): List<AreaCategoriesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAreaCategories(category: AreaCategoriesEntity)

    @Query("SELECT * FROM food_category_entity")
    suspend fun getAllFoodCategories(): List<FoodCategoriesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodCategories(category: FoodCategoriesEntity)

    @Query("SELECT * FROM bookmark_entity")
    fun getBookmarkedMeal(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkMeal(bookmarkedMeal: BookmarkEntity)

    @Query("DELETE FROM bookmark_entity WHERE id=:id")
    suspend fun deleteBookmarkedMeal(id: String)
}