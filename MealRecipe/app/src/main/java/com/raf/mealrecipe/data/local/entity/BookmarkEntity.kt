package com.raf.mealrecipe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_entity")
data class BookmarkEntity(
    @PrimaryKey
    val id: String,
    val meal: String,
    val thumbnail: String
)