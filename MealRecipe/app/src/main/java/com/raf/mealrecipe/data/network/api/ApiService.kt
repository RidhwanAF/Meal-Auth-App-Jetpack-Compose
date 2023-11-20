package com.raf.mealrecipe.data.network.api

import com.raf.mealrecipe.data.network.response.CategoriesAreaResponse
import com.raf.mealrecipe.data.network.response.CategoriesFoodResponse
import com.raf.mealrecipe.data.network.response.GetFoodDetailResponse
import com.raf.mealrecipe.data.network.response.GetFoodListByCategoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("list.php?a=list")
    suspend fun getCategoriesByArea(): CategoriesAreaResponse

    @GET("list.php?c=list")
    suspend fun getCategoriesByFood(): CategoriesFoodResponse

    @GET("filter.php")
    suspend fun getFoodListByCategory(
        @Query("c") category: String
    ): GetFoodListByCategoryResponse

    @GET("filter.php")
    suspend fun getFoodListByArea(
        @Query("a") category: String
    ): GetFoodListByCategoryResponse

    @GET("lookup.php")
    suspend fun getFoodDetail(
        @Query("i") id: String
    ): GetFoodDetailResponse
}