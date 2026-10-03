package com.smarttranslator.app

import android.content.Context
import android.content.Intent
import com.smarttranslator.app.service.FloatingTranslatorService

object AppActions {
    fun startFloatingOverlay(context: Context) {
        val intent = Intent(context, FloatingTranslatorService::class.java)
        context.startService(intent)
    }
}
