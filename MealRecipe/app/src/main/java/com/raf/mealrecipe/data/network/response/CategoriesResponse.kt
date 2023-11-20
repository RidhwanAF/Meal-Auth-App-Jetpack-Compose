package com.raf.mealrecipe.data.network.response

import com.google.gson.annotations.SerializedName

data class CategoriesAreaResponse(

	@field:SerializedName("meals")
	val meals: List<AreaMealsItem>
)

data class AreaMealsItem(

	@field:SerializedName("strArea")
	val strArea: String
)


data class CategoriesFoodResponse(

	@field:SerializedName("meals")
	val meals: List<FoodMealsItem>
)

data class FoodMealsItem(

	@field:SerializedName("strCategory")
	val strCategory: String
)

