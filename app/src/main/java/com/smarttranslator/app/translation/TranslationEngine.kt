package com.smarttranslator.app.translation

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.smarttranslator.app.model.Language
import com.smarttranslator.app.model.SupportedLanguages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TranslationEngine {

    suspend fun translateText(
        sourceText: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): String = withContext(Dispatchers.IO) {
        if (sourceText.isBlank()) return@withContext ""
        if (sourceLanguage.code == targetLanguage.code) return@withContext sourceText.trim()

        try {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage.code)
                .setTargetLanguage(targetLanguage.code)
                .build()

            val translator = Translation.getClient(options)
            try {
                Tasks.await(translator.downloadModelIfNeeded())
                Tasks.await(translator.translate(sourceText.trim()))
            } finally {
                translator.close()
            }
        } catch (e: Exception) {
            Log.e("TranslationEngine", "Translation failed: ${e.message}", e)
            val fallback = "[$sourceLanguage -> $targetLanguage] ${sourceText.trim()}"
            fallback
        }
    }

    fun detectLanguage(text: String): Language {
        if (text.isBlank()) return SupportedLanguages.list.first()
        return SupportedLanguages.list.first { it.code == "ar" }
    }
}
