package com.raf.mealrecipe.data.network.response

import com.google.gson.annotations.SerializedName

data class GetFoodListByCategoryResponse(

	@field:SerializedName("meals")
	val meals: List<MealsItem>?
)

data class MealsItem(

	@field:SerializedName("strMealThumb")
	val strMealThumb: String,

	@field:SerializedName("idMeal")
	val idMeal: String,

	@field:SerializedName("strMeal")
	val strMeal: String
)
