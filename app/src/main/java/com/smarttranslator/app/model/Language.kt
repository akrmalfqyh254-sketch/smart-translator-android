package com.smarttranslator.app.model

import androidx.annotation.StringRes

data class Language(
    val code: String,
    val label: String,
    val nativeLabel: String,
    val isAuto: Boolean = false
)

object SupportedLanguages {
    val list = listOf(
        Language("auto", "Auto Detect", "تحديد تلقائي", true),
        Language("ar", "Arabic", "العربية"),
        Language("en", "English", "الإنجليزية"),
        Language("fr", "French", "الفرنسية"),
        Language("de", "German", "الألمانية"),
        Language("es", "Spanish", "الإسبانية"),
        Language("it", "Italian", "الإيطالية"),
        Language("tr", "Turkish", "التركية"),
        Language("ur", "Urdu", "الأردية"),
        Language("zh", "Chinese", "الصينية")
    )
}
