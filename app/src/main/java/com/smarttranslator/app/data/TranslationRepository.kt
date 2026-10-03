package com.smarttranslator.app.data

class TranslationRepository(
    private val translationDao: TranslationDao
) {
    suspend fun saveTranslation(item: TranslationEntity) {
        translationDao.insert(item)
    }

    suspend fun getHistory(): List<TranslationEntity> = translationDao.getAll()

    suspend fun getFavorites(): List<TranslationEntity> = translationDao.getFavorites()
}
