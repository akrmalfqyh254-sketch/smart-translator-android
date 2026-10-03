package com.smarttranslator.app.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.smarttranslator.app.R

class FloatingTranslatorService : Service() {

    private var windowManager: WindowManager? = null
    private var overlayView: LinearLayout? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WindowManager::class.java)
        val inflater = LayoutInflater.from(this)
        overlayView = inflater.inflate(R.layout.floating_translation_overlay, null) as LinearLayout

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 40
            y = 200
        }

        windowManager?.addView(overlayView, params)

        val textView = overlayView?.findViewById<TextView>(R.id.translationText)
        val closeButton = overlayView?.findViewById<Button>(R.id.closeButton)
        val copyButton = overlayView?.findViewById<Button>(R.id.copyButton)

        textView?.text = "الترجمة العائمة\nمستعدة للتفعيل"
        closeButton?.setOnClickListener { stopSelf() }
        copyButton?.setOnClickListener { /* future copy action */ }
    }

    override fun onDestroy() {
        super.onDestroy()
        overlayView?.let { view ->
            windowManager?.removeViewImmediate(view)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
