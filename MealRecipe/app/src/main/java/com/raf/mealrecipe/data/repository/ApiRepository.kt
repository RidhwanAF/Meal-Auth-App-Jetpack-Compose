package com.raf.mealrecipe.data.repository

import com.raf.mealrecipe.data.network.api.ApiService
import com.raf.mealrecipe.data.network.response.CategoriesAreaResponse
import com.raf.mealrecipe.data.network.response.CategoriesFoodResponse
import com.raf.mealrecipe.data.network.response.GetFoodDetailResponse
import com.raf.mealrecipe.data.network.response.GetFoodListByCategoryResponse
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getMealCategoriesByArea(): CategoriesAreaResponse {
        return apiService.getCategoriesByArea()
    }

    suspend fun getMealCategoriesByFood(): CategoriesFoodResponse {
        return apiService.getCategoriesByFood()
    }

    suspend fun getFoodListByCategory(category: String): GetFoodListByCategoryResponse {
        return apiService.getFoodListByCategory(category)
    }

    suspend fun getFoodListByArea(category: String): GetFoodListByCategoryResponse {
        return apiService.getFoodListByArea(category)
    }

    suspend fun getFoodDetail(id: String): GetFoodDetailResponse {
        return apiService.getFoodDetail(id)
    }
}