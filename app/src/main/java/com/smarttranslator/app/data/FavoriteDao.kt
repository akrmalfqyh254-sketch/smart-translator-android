package com.smarttranslator.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: com.smarttranslator.app.model.FavoriteItem)

    @Query("SELECT * FROM favorite_items ORDER BY createdAt DESC")
    suspend fun getAll(): List<com.smarttranslator.app.model.FavoriteItem>
}
