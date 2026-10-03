package com.smarttranslator.app.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [TranslationEntity::class], version = 1, exportSchema = false)
abstract class SmartTranslatorDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao

    companion object {
        @Volatile
        private var INSTANCE: SmartTranslatorDatabase? = null

        fun getDatabase(context: Context): SmartTranslatorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartTranslatorDatabase::class.java,
                    "smart_translator_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
