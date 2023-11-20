package com.raf.mealrecipe.data.repository

import com.raf.mealrecipe.data.local.db.MealRecipeDatabase
import com.raf.mealrecipe.data.local.entity.AreaCategoriesEntity
import com.raf.mealrecipe.data.local.entity.BookmarkEntity
import com.raf.mealrecipe.data.local.entity.FoodCategoriesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDbRepository @Inject constructor(db: MealRecipeDatabase) {

    private val dao = db.mealRecipeDao()

    suspend fun getAllCategories(): Pair<List<FoodCategoriesEntity>, List<AreaCategoriesEntity>> {
        val areaCategories = dao.getAllAreaCategories()
        val foodCategories = dao.getAllFoodCategories()
        return Pair(foodCategories, areaCategories)
    }

    suspend fun insertFoodCategories(categoriesEntity: FoodCategoriesEntity) {
        dao.insertFoodCategories(categoriesEntity)
    }

    suspend fun insertAreaCategories(categoriesEntity: AreaCategoriesEntity) {
        dao.insertAreaCategories(categoriesEntity)
    }

    suspend fun insertBookmarkMeal(bookmarkMeal: BookmarkEntity) =
        dao.insertBookmarkMeal(bookmarkMeal)

    suspend fun deleteBookmarkedMeal(id: String) =
        dao.deleteBookmarkedMeal(id)

    fun getBookmarkedMeal(): Flow<List<BookmarkEntity>> =
        dao.getBookmarkedMeal()
}