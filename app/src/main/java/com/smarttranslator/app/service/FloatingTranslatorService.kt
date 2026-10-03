package com.smarttranslator.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.smarttranslator.app.R
import java.util.Locale

class FloatingTranslatorService : Service() {

    private companion object {
        private const val CHANNEL_ID = "smart_translator_overlay"
        private const val NOTIFICATION_ID = 1001
    }

    private var windowManager: WindowManager? = null
    private var overlayView: LinearLayout? = null
    private var params: WindowManager.LayoutParams? = null
    private var initialX = 0
    private var initialY = 0
    private var touchStartX = 0f
    private var touchStartY = 0f
    private var currentText: String = "الترجمة العائمة\nجاهزة"
    private var textToSpeech: TextToSpeech? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())

        windowManager = getSystemService(WindowManager::class.java)
        val inflater = LayoutInflater.from(this)
        overlayView = inflater.inflate(R.layout.floating_translation_overlay, null) as LinearLayout

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
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
        val speakButton = overlayView?.findViewById<Button>(R.id.speakButton)

        textView?.text = currentText

        overlayView?.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params?.x ?: 0
                    initialY = params?.y ?: 0
                    touchStartX = event.rawX
                    touchStartY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = (event.rawX - touchStartX).toInt()
                    val deltaY = (event.rawY - touchStartY).toInt()
                    params?.x = initialX + deltaX
                    params?.y = initialY + deltaY
                    windowManager?.updateViewLayout(overlayView, params)
                }
            }
            true
        }

        closeButton?.setOnClickListener { stopSelf() }
        copyButton?.setOnClickListener { copyCurrentText() }
        speakButton?.setOnClickListener { speakCurrentText() }

        initTextToSpeech()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val text = intent?.getStringExtra("overlay_text") ?: currentText
        updateOverlayText(text)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        overlayView?.let { view ->
            windowManager?.removeViewImmediate(view)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun updateOverlayText(text: String) {
        currentText = text.ifBlank { "الترجمة العائمة\nجاهزة" }
        overlayView?.findViewById<TextView>(R.id.translationText)?.text = currentText
    }

    private fun copyCurrentText() {
        if (currentText.isBlank()) return
        val clipboard = getSystemService(ClipboardManager::class.java)
        val clip = ClipData.newPlainText("smart_translator_text", currentText)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(this, "تم نسخ النص", Toast.LENGTH_SHORT).show()
    }

    private fun speakCurrentText() {
        if (currentText.isBlank()) return
        textToSpeech?.speak(
            currentText,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "smart_translator_overlay"
        )
    }

    private fun initTextToSpeech() {
        textToSpeech = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale("ar")
                val result = textToSpeech?.setLanguage(locale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w("FloatingOverlay", "Arabic TTS language not supported on this device")
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Smart Translator Overlay",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Smart Translator")
            .setContentText("خدمة الترجمة العائمة نشطة")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}
