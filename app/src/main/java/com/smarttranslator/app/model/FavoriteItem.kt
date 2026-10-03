package com.smarttranslator.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_items")
data class FavoriteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val sourceLanguageCode: String,
    val targetLanguageCode: String,
    val createdAt: Long = System.currentTimeMillis()
)
