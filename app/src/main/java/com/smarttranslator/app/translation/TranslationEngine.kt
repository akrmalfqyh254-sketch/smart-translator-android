package com.smarttranslator.app.translation

import com.smarttranslator.app.model.Language
import com.smarttranslator.app.model.SupportedLanguages

class TranslationEngine {
    fun translateText(
        sourceText: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): String {
        if (sourceText.isBlank()) return ""
        if (sourceLanguage.code == targetLanguage.code) return sourceText.trim()

        val samples = mapOf(
            "ar" to "AR",
            "en" to "EN",
            "fr" to "FR",
            "de" to "DE",
            "es" to "ES",
            "it" to "IT",
            "tr" to "TR",
            "ur" to "UR",
            "zh" to "ZH"
        )

        val from = samples[sourceLanguage.code] ?: "XX"
        val to = samples[targetLanguage.code] ?: "YY"
        return "[$from → $to] ${sourceText.trim()}"
    }

    fun detectLanguage(text: String): Language {
        if (text.isBlank()) return SupportedLanguages.list.first()
        return SupportedLanguages.list.first { it.code == "ar" }
    }
}
