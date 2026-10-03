package com.smarttranslator.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class SmartAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED or
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val rootNode = rootInActiveWindow ?: return
        val text = collectTextFromNode(rootNode)
        if (text.isNotBlank()) {
            // Placeholder: in a production build this would send extracted text to the floating bubble
            // and trigger translation using the active source/target languages.
        }
    }

    override fun onInterrupt() = Unit

    private fun collectTextFromNode(node: AccessibilityNodeInfo): String {
        val builder = StringBuilder()
        if (node.text != null && node.text.toString().isNotBlank()) {
            builder.append(node.text.toString()).append(" ")
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            builder.append(collectTextFromNode(child))
        }
        return builder.toString()
    }
}
