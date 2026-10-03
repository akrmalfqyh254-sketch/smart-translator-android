package com.smarttranslator.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Enhanced AccessibilityService that collects visible text from the active window,
 * debounces rapid updates, filters out the app's own package, and forwards a
 * cleaned summary to the FloatingTranslatorService via Intent extras so the
 * overlay can display the detected text.
 *
 * Notes:
 * - This service only reads text available through AccessibilityNodeInfo. Some
 *   apps may not expose text nodes depending on their implementation.
 * - The service protects against flooding by debouncing updates and only
 *   sending when the text meaningfully changes.
 */
class SmartAccessibilityService : AccessibilityService() {

    private val handler = Handler(Looper.getMainLooper())
    private var pendingRunnable: Runnable? = null
    private var lastSentText: String? = null

    // Debounce interval to avoid updating the overlay too often
    private val DEBOUNCE_MS = 800L

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            // Consider setting packageNames to limit monitored packages if desired
        }
        Log.d("SmartAccessibility", "service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Ignore events coming from our own app to avoid loops
        val pkg = event.packageName?.toString() ?: ""
        if (pkg.startsWith("com.smarttranslator")) return

        val root = rootInActiveWindow ?: return

        // Collect visible text from the active window
        val raw = collectTextFromNode(root)
        val cleaned = raw.replace(Regex("\\s+"), " ").trim()

        if (cleaned.isBlank()) return
        if (cleaned == lastSentText) return

        // Debounce updates so we don't spam the overlay/service
        pendingRunnable?.let { handler.removeCallbacks(it) }
        val runnable = Runnable {
            try {
                val toSend = cleaned.take(300) // limit size for overlay readability
                lastSentText = toSend

                val intent = Intent(this, FloatingTranslatorService::class.java).apply {
                    putExtra("overlay_text", toSend)
                }

                // Use startService; FloatingTranslatorService is a foreground service so this is allowed
                startService(intent)
                Log.d("SmartAccessibility", "forwarded text to overlay: ${toSend}")
            } catch (t: Throwable) {
                Log.w("SmartAccessibility", "failed to forward overlay text: ${t.message}", t)
            }
        }

        pendingRunnable = runnable
        handler.postDelayed(runnable, DEBOUNCE_MS)
    }

    override fun onInterrupt() {
        // Clear any pending work
        pendingRunnable?.let { handler.removeCallbacks(it) }
        pendingRunnable = null
    }

    // Safe traversal that avoids extremely deep recursion by limiting depth
    private fun collectTextFromNode(node: AccessibilityNodeInfo?, depth: Int = 0): String {
        if (node == null) return ""
        if (depth > 50) return "" // guard against pathological view hierarchies

        val builder = StringBuilder()

        try {
            if (!node.text.isNullOrBlank()) {
                builder.append(node.text).append(' ')
            }

            val childCount = node.childCount
            for (i in 0 until childCount) {
                val child = node.getChild(i) ?: continue
                builder.append(collectTextFromNode(child, depth + 1))
            }
        } catch (t: Throwable) {
            Log.w("SmartAccessibility", "error while traversing node: ${t.message}")
        }

        return builder.toString()
    }
}
