package com.raf.mealrecipe.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "area_categories_entity")
data class AreaCategoriesEntity(
    @PrimaryKey
    @ColumnInfo("category_area")
    val categoryArea: String
)

@Entity(tableName = "food_category_entity")
data class FoodCategoriesEntity(
    @PrimaryKey()
    @ColumnInfo("category_food")
    val categoryFood: String,
)