package com.smarttranslator.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TranslationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TranslationEntity): Long

    @Query("SELECT * FROM translations ORDER BY created_at DESC")
    suspend fun getAll(): List<TranslationEntity>

    @Query("SELECT * FROM translations WHERE is_favorite = 1 ORDER BY created_at DESC")
    suspend fun getFavorites(): List<TranslationEntity>
}
