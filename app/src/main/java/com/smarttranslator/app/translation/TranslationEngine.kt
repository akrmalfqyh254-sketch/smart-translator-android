package com.smarttranslator.app.translation

import android.content.Context

class TranslationEngine(private val context: Context) {
    fun translateText(sourceText: String, sourceLanguage: String, targetLanguage: String): String {
        return when {
            sourceLanguage == targetLanguage -> sourceText
            sourceText.isBlank() -> ""
            else -> "[$sourceLanguage -> $targetLanguage] ${sourceText.trim()}"
        }
    }
}
