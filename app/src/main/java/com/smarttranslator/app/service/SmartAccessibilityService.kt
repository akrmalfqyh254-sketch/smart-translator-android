package com.smarttranslator.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class SmartAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val rootNode = rootInActiveWindow ?: return
        val text = collectTextFromNode(rootNode)
        if (text.isNotBlank()) {
            // Future implementation: read current app text and show floating translation overlay.
        }
    }

    override fun onInterrupt() = Unit

    private fun collectTextFromNode(node: AccessibilityNodeInfo): String {
        val builder = StringBuilder()
        val children = node.childCount
        for (i in 0 until children) {
            val child = node.getChild(i) ?: continue
            if (child.text != null) {
                builder.append(child.text).append(" ")
            }
            builder.append(collectTextFromNode(child))
        }
        return builder.toString()
    }
}
