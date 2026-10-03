package com.smarttranslator.app

import android.content.Context
import android.content.Intent
import com.smarttranslator.app.service.FloatingTranslatorService

object AppActions {
    fun startFloatingOverlay(context: Context, text: String = "الترجمة العائمة") {
        val intent = Intent(context, FloatingTranslatorService::class.java).apply {
            putExtra("overlay_text", text)
        }
        context.startService(intent)
    }
}
